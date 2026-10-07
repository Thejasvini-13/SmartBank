/* =========================================================
   SmartBank Security & Suspicious Transactions Logic
   ========================================================= */

document.addEventListener("DOMContentLoaded", () => {
    loadSuspiciousTransactions();
});

async function loadSuspiciousTransactions() {
    const tableBody = document.getElementById("suspicious-table-body");
    if (!tableBody) return;

    try {
        const list = await API.getSuspiciousTransactions();
        tableBody.innerHTML = "";

        if (list.length === 0) {
            tableBody.innerHTML = "<tr><td colspan='6' style='text-align:center;'>No suspicious transactions detected. System status: SECURE.</td></tr>";
            return;
        }

        list.forEach(tx => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>#TX-${tx.transactionId}</td>
                <td><strong>${tx.accountNumber}</strong></td>
                <td class="text-debit">₹${tx.amount.toLocaleString('en-IN')}</td>
                <td><span class="badge badge-danger">HIGH RISK</span></td>
                <td><small style="color:#b91c1c;">${tx.riskReason}</small></td>
                <td>${tx.timestamp.replace("T", " ").substring(0, 19)}</td>
            `;
            tableBody.appendChild(row);
        });
    } catch (err) {
        showToast("Error loading suspicious transaction audit logs: " + err.message, "danger");
    }
}
