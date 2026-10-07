/* =========================================================
   SmartBank Dashboard Logic
   ========================================================= */

document.addEventListener("DOMContentLoaded", () => {
    loadDashboardData();
});

async function loadDashboardData() {
    try {
        const accounts = await API.getAccounts();
        const transactions = await API.getTransactions();
        const goals = await API.getGoals();

        // 1. Calculate Balances & Monthly Spending
        let totalBalance = 0;
        accounts.forEach(acc => totalBalance += acc.balance);

        let monthlySpending = 0;
        transactions.forEach(tx => {
            if (tx.type === "DEBIT") monthlySpending += tx.amount;
        });

        let totalSavingsGoals = 0;
        goals.forEach(g => totalSavingsGoals += g.currentAmount);

        document.getElementById("total-balance").innerText = `₹${totalBalance.toLocaleString('en-IN')}`;
        document.getElementById("monthly-spending").innerText = `₹${monthlySpending.toLocaleString('en-IN')}`;
        document.getElementById("savings-total").innerText = `₹${totalSavingsGoals.toLocaleString('en-IN')}`;

        // 2. Financial Health Score Quick Fetch
        const health = await API.calculateHealth({
            monthlyIncome: 65000,
            monthlyExpenses: monthlySpending,
            totalBalance: totalBalance,
            existingEmi: 12500,
            goalProgressPercent: 55,
            spendingLimitUsagePercent: 86
        });

        document.getElementById("health-score").innerText = `${health.overallScore} / 100`;
        const categoryBadge = document.getElementById("health-category");
        if (categoryBadge) {
            categoryBadge.innerText = health.category;
            categoryBadge.className = `badge ${health.overallScore >= 80 ? 'badge-success' : health.overallScore >= 60 ? 'badge-info' : 'badge-warning'}`;
        }

        // 3. Render Recent Transactions Table (Top 5)
        const txContainer = document.getElementById("recent-transactions-list");
        if (txContainer) {
            txContainer.innerHTML = "";
            const recent = transactions.slice(0, 5);

            if (recent.length === 0) {
                txContainer.innerHTML = "<tr><td colspan='4' style='text-align:center;'>No transactions recorded yet.</td></tr>";
            } else {
                recent.forEach(tx => {
                    const row = document.createElement("tr");
                    const isCredit = tx.type === "CREDIT";
                    row.innerHTML = `
                        <td><strong>${tx.description || tx.category}</strong><br><small style="color:#64748b;">${tx.timestamp.replace("T", " ").substring(0, 16)}</small></td>
                        <td><span class="badge badge-info">${tx.category}</span></td>
                        <td>${tx.accountNumber}</td>
                        <td class="${isCredit ? 'text-credit' : 'text-debit'}">${isCredit ? '+' : '-'}₹${tx.amount.toLocaleString('en-IN')}</td>
                    `;
                    txContainer.appendChild(row);
                });
            }
        }

        // 4. Render Primary Savings Goal Widget
        const goalWidget = document.getElementById("primary-goal-widget");
        if (goalWidget && goals.length > 0) {
            const g = goals[0];
            const pct = Math.min(100, Math.round((g.currentAmount / g.targetAmount) * 100));
            goalWidget.innerHTML = `
                <div style="display:flex; justify-content:space-between; margin-bottom:8px;">
                    <strong>${g.title}</strong>
                    <span style="color:#64748b; font-size:0.9rem;">₹${g.currentAmount.toLocaleString('en-IN')} / ₹${g.targetAmount.toLocaleString('en-IN')}</span>
                </div>
                <div class="progress-bar-bg">
                    <div class="progress-bar-fill" style="width: ${pct}%;"></div>
                </div>
                <div style="margin-top:8px; font-size:0.85rem; color:#64748b;">${pct}% completed (Deadline: ${g.deadline})</div>
            `;
        }

        // 5. Render Spending Limit Widget
        try {
            const limit = await API.getSpendingLimitStatus("SB1001");
            const limitWidget = document.getElementById("limit-widget");
            if (limitWidget) {
                const fillClass = limit.alertLevel === "CRITICAL" || limit.alertLevel === "LIMIT EXCEEDED" ? "danger" : limit.alertLevel === "WARNING" ? "warning" : "";
                limitWidget.innerHTML = `
                    <div style="display:flex; justify-content:space-between; margin-bottom:8px;">
                        <strong>Monthly Limit (SB1001)</strong>
                        <span>₹${limit.currentSpending.toLocaleString('en-IN')} / ₹${limit.monthlyLimit.toLocaleString('en-IN')}</span>
                    </div>
                    <div class="progress-bar-bg">
                        <div class="progress-bar-fill ${fillClass}" style="width: ${Math.min(100, limit.usagePercentage)}%;"></div>
                    </div>
                    <div style="margin-top:8px; display:flex; justify-content:space-between; font-size:0.85rem;">
                        <span>${limit.usagePercentage}% used</span>
                        <span class="badge ${limit.alertLevel === 'NORMAL' ? 'badge-success' : limit.alertLevel === 'WARNING' ? 'badge-warning' : 'badge-danger'}">${limit.alertLevel}</span>
                    </div>
                `;
            }
        } catch (e) {
            console.log("No spending limit set for SB1001 yet.");
        }

    } catch (err) {
        showToast("Error loading dashboard data: " + err.message, "danger");
    }
}
