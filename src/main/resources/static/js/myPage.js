
function fillModal(button) {
    var modalContent = document.getElementById('deleteModalContent');
    var id = button.getAttribute('data-id');
    var medication = button.getAttribute('data-medication');

    document.getElementById('medicationIdInput').value = id;

    modalContent.innerHTML = 'Item ID: ' + id + '<br>Item Name: ' + medication;
}

function updateMedicationModal(button){
    var id = button.getAttribute('data-id');
    document.getElementById('medicationUpdateIdInput').value = id;
}
