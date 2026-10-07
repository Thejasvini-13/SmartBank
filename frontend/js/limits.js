/* =========================================================
   SmartBank Spending Limit Alerts Logic
   ========================================================= */

document.addEventListener("DOMContentLoaded", () => {
    loadLimitDetails();

    document.getElementById("set-limit-form")?.addEventListener("submit", async (e) => {
        e.preventDefault();
        const accountNumber = document.getElementById("limit-acc-no").value;
        const monthlyLimit = parseFloat(document.getElementById("limit-amount").value);

        try {
            await API.setSpendingLimit({ accountNumber, monthlyLimit, currentSpending: 0 });
            showToast("Monthly spending limit saved!");
            loadLimitDetails();
        } catch (err) {
            showToast(err.message, "danger");
        }
    });
});

async function loadLimitDetails() {
    const accNo = document.getElementById("limit-acc-no")?.value || "SB1001";
    const statusContainer = document.getElementById("limit-status-card");
    if (!statusContainer) return;

    try {
        const res = await API.getSpendingLimitStatus(accNo);
        const alertClass = res.alertLevel === "CRITICAL" || res.alertLevel === "LIMIT EXCEEDED" ? "badge-danger" : res.alertLevel === "WARNING" ? "badge-warning" : "badge-success";
        const fillClass = res.alertLevel === "CRITICAL" || res.alertLevel === "LIMIT EXCEEDED" ? "danger" : res.alertLevel === "WARNING" ? "warning" : "";

        statusContainer.innerHTML = `
            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:16px;">
                <h3 style="font-size:1.1rem; color:#0f172a;">Account ${res.accountNumber} Spending Monitor</h3>
                <span class="badge ${alertClass}">${res.alertLevel}</span>
            </div>
            <div style="font-size:2rem; font-weight:700; color:#0f172a; margin-bottom:8px;">
                ₹${res.currentSpending.toLocaleString('en-IN')} <span style="font-size:1rem; color:#64748b; font-weight:400;">/ ₹${res.monthlyLimit.toLocaleString('en-IN')} limit</span>
            </div>
            <div class="progress-bar-bg" style="height:14px;">
                <div class="progress-bar-fill ${fillClass}" style="width: ${Math.min(100, res.usagePercentage)}%;"></div>
            </div>
            <div style="margin-top:12px; font-weight:600; color:#334155; font-size:0.95rem;">
                ${res.usagePercentage}% of monthly limit consumed
            </div>
            <p style="margin-top:12px; font-size:0.9rem; color:#64748b;">${res.message}</p>
        `;
    } catch (err) {
        statusContainer.innerHTML = "<p>No spending limit configured for this account yet. Set one above!</p>";
    }
}
