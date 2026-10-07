/* =========================================================
   SmartBank Financial Health Score Page Logic
   ========================================================= */

document.addEventListener("DOMContentLoaded", () => {
    loadFinancialHealth();

    document.getElementById("health-recalc-form")?.addEventListener("submit", async (e) => {
        e.preventDefault();
        const req = {
            monthlyIncome: parseFloat(document.getElementById("h-income").value),
            monthlyExpenses: parseFloat(document.getElementById("h-expenses").value),
            totalBalance: parseFloat(document.getElementById("h-balance").value),
            existingEmi: parseFloat(document.getElementById("h-emi").value || 0),
            goalProgressPercent: parseFloat(document.getElementById("h-goal-pct").value || 50),
            spendingLimitUsagePercent: parseFloat(document.getElementById("h-limit-pct").value || 50)
        };

        try {
            const report = await API.calculateHealth(req);
            renderHealthReport(report);
            showToast("Financial Health Score recalculated!");
        } catch (err) {
            showToast(err.message, "danger");
        }
    });
});

async function loadFinancialHealth() {
    try {
        const report = await API.calculateHealth({
            monthlyIncome: 65000,
            monthlyExpenses: 28450,
            totalBalance: 124500,
            existingEmi: 12500,
            goalProgressPercent: 55,
            spendingLimitUsagePercent: 86
        });
        renderHealthReport(report);
    } catch (err) {
        showToast("Error loading health score: " + err.message, "danger");
    }
}

function renderHealthReport(report) {
    document.getElementById("health-total-score").innerText = report.overallScore;
    document.getElementById("health-category-title").innerText = report.category;

    renderBar("bar-savings", report.savingsScore);
    renderBar("bar-spending", report.spendingScore);
    renderBar("bar-debt", report.debtScore);
    renderBar("bar-goals", report.goalScore);

    const recList = document.getElementById("health-recommendations");
    if (recList) {
        recList.innerHTML = "";
        report.recommendations.forEach(rec => {
            const li = document.createElement("li");
            li.innerText = rec;
            li.style.marginBottom = "8px";
            recList.appendChild(li);
        });
    }
}

function renderBar(elementId, val) {
    const el = document.getElementById(elementId);
    if (!el) return;
    el.style.width = `${val}%`;
    const label = document.getElementById(`${elementId}-val`);
    if (label) label.innerText = `${val}%`;
}
