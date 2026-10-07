/* =========================================================
   SmartBank Scheduled Payments Logic
   ========================================================= */

document.addEventListener("DOMContentLoaded", () => {
    loadScheduledPayments();

    document.getElementById("schedule-payment-form")?.addEventListener("submit", async (e) => {
        e.preventDefault();
        const accountNumber = document.getElementById("sched-acc-no").value;
        const payeeName = document.getElementById("sched-payee").value;
        const amount = parseFloat(document.getElementById("sched-amount").value);
        const frequency = document.getElementById("sched-freq").value;
        const nextPaymentDate = document.getElementById("sched-date").value;

        try {
            await API.schedulePayment({ accountNumber, payeeName, amount, frequency, nextPaymentDate, status: "ACTIVE" });
            showToast("Recurring payment scheduled successfully!");
            closeModal('schedule-modal');
            loadScheduledPayments();
        } catch (err) {
            showToast(err.message, "danger");
        }
    });
});

async function loadScheduledPayments() {
    const tableBody = document.getElementById("scheduled-payments-body");
    if (!tableBody) return;

    try {
        const payments = await API.getScheduledPayments();
        tableBody.innerHTML = "";

        if (payments.length === 0) {
            tableBody.innerHTML = "<tr><td colspan='6' style='text-align:center;'>No active scheduled payments.</td></tr>";
            return;
        }

        payments.forEach(p => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td><strong>${p.payeeName}</strong></td>
                <td>${p.accountNumber}</td>
                <td>₹${p.amount.toLocaleString('en-IN')}</td>
                <td><span class="badge badge-info">${p.frequency}</span></td>
                <td>${p.nextPaymentDate}</td>
                <td>
                    <button class="btn btn-danger" style="padding:4px 10px; font-size:0.8rem;" onclick="cancelPayment(${p.paymentId})">Cancel</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (err) {
        showToast("Error loading scheduled payments: " + err.message, "danger");
    }
}

async function cancelPayment(id) {
    if (confirm("Are you sure you want to cancel this scheduled payment?")) {
        try {
            await API.cancelScheduledPayment(id);
            showToast("Scheduled payment cancelled.");
            loadScheduledPayments();
        } catch (err) {
            showToast(err.message, "danger");
        }
    }
}
