/* =========================================================
   SmartBank Savings Goal Tracker Page Logic
   ========================================================= */

document.addEventListener("DOMContentLoaded", () => {
    loadSavingsGoals();

    document.getElementById("create-goal-form")?.addEventListener("submit", async (e) => {
        e.preventDefault();
        const title = document.getElementById("goal-title").value;
        const targetAmount = parseFloat(document.getElementById("goal-target").value);
        const currentAmount = parseFloat(document.getElementById("goal-current").value || 0);
        const deadline = document.getElementById("goal-deadline").value;

        try {
            await API.createGoal({ customerId: 1, title, targetAmount, currentAmount, deadline });
            showToast("Savings Goal created!");
            closeModal('goal-modal');
            loadSavingsGoals();
        } catch (err) {
            showToast(err.message, "danger");
        }
    });

    document.getElementById("add-savings-form")?.addEventListener("submit", async (e) => {
        e.preventDefault();
        const id = document.getElementById("deposit-goal-id").value;
        const amount = parseFloat(document.getElementById("deposit-goal-amount").value);

        try {
            await API.addGoalDeposit(id, amount);
            showToast(`Added ₹${amount} towards goal!`);
            closeModal('deposit-goal-modal');
            loadSavingsGoals();
        } catch (err) {
            showToast(err.message, "danger");
        }
    });
});

async function loadSavingsGoals() {
    const grid = document.getElementById("goals-grid");
    if (!grid) return;

    try {
        const goals = await API.getGoals();
        grid.innerHTML = "";

        if (goals.length === 0) {
            grid.innerHTML = "<div class='card'><p>No active savings goals found. Create one now!</p></div>";
            return;
        }

        goals.forEach(g => {
            const remaining = Math.max(0, g.targetAmount - g.currentAmount);
            const pct = Math.min(100, Math.round((g.currentAmount / g.targetAmount) * 100));

            const card = document.createElement("div");
            card.className = "card";
            card.innerHTML = `
                <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:12px;">
                    <strong style="font-size:1.15rem; color:#0f172a;">${g.title}</strong>
                    <span class="badge badge-info">${pct}% Progress</span>
                </div>
                <div style="font-size:1.5rem; font-weight:700; color:#2563eb; margin-bottom:4px;">
                    ₹${g.currentAmount.toLocaleString('en-IN')} <span style="font-size:0.9rem; color:#64748b; font-weight:400;">/ ₹${g.targetAmount.toLocaleString('en-IN')}</span>
                </div>
                <div class="progress-bar-bg">
                    <div class="progress-bar-fill" style="width: ${pct}%;"></div>
                </div>
                <div style="margin-top:16px; display:flex; justify-content:space-between; font-size:0.85rem; color:#64748b;">
                    <span>Remaining: ₹${remaining.toLocaleString('en-IN')}</span>
                    <span>Deadline: ${g.deadline}</span>
                </div>
                <div style="margin-top:16px; display:flex; gap:8px;">
                    <button class="btn btn-primary" style="flex:1;" onclick="openDepositModal(${g.goalId}, '${g.title}')">+ Add Funds</button>
                </div>
            `;
            grid.appendChild(card);
        });
    } catch (err) {
        showToast("Error loading savings goals: " + err.message, "danger");
    }
}

function openDepositModal(goalId, title) {
    document.getElementById("deposit-goal-id").value = goalId;
    document.getElementById("deposit-goal-title-display").innerText = title;
    openModal('deposit-goal-modal');
}
