
function updateRideRequestModal(button) {
    var id = button.getAttribute('data-id');
    console.log(id + ' : this is the value of the id');
    document.getElementById('rideUpdateId').value = id;
}
