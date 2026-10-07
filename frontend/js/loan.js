/* =========================================================
   SmartBank Loan Eligibility Calculator Logic
   ========================================================= */

document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("loan-calc-form")?.addEventListener("submit", async (e) => {
        e.preventDefault();

        const loanReq = {
            customerId: 1,
            monthlyIncome: parseFloat(document.getElementById("loan-income").value),
            monthlyExpenses: parseFloat(document.getElementById("loan-expenses").value),
            existingEmi: parseFloat(document.getElementById("loan-emi").value || 0),
            creditScore: parseInt(document.getElementById("loan-score").value),
            requestedAmount: parseFloat(document.getElementById("loan-amount").value),
            tenureMonths: parseInt(document.getElementById("loan-tenure").value)
        };

        try {
            const result = await API.calculateLoan(loanReq);
            renderLoanResult(result);
        } catch (err) {
            showToast("Loan evaluation error: " + err.message, "danger");
        }
    });
});

function renderLoanResult(res) {
    const card = document.getElementById("loan-result-card");
    if (!card) return;

    card.style.display = "block";
    const isEligible = res.status === "ELIGIBLE";

    card.innerHTML = `
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:16px;">
            <h3 style="font-size:1.25rem; color:#0f172a;">Loan Assessment Result</h3>
            <span class="badge ${isEligible ? 'badge-success' : 'badge-danger'}" style="font-size:0.9rem; padding:6px 14px;">${res.status}</span>
        </div>
        <div class="stats-grid" style="margin-bottom:16px;">
            <div>
                <div class="card-subtitle">Estimated Monthly EMI</div>
                <div class="card-value" style="font-size:1.4rem; color:#2563eb;">₹${res.estimatedEmi.toLocaleString('en-IN')}</div>
            </div>
            <div>
                <div class="card-subtitle">Debt-to-Income Ratio</div>
                <div class="card-value" style="font-size:1.4rem; color:${res.debtToIncomeRatio <= 50 ? '#16a34a' : '#dc2626'};">${res.debtToIncomeRatio}%</div>
            </div>
        </div>
        <div style="background-color:#f8fafc; padding:16px; border-radius:8px; border:1px solid #e2e8f0;">
            <strong style="color:#334155;">Assessment Rationale:</strong>
            <p style="margin-top:6px; font-size:0.9rem; color:#64748b; line-height:1.5;">${res.explanation}</p>
        </div>
        <p style="margin-top:16px; font-size:0.75rem; color:#94a3b8; font-style:italic;">
            Note: This calculation is an educational rule-based simulation and does not constitute a legal or binding banking credit agreement.
        </p>
    `;
}
