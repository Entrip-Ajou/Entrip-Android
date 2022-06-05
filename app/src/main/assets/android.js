function showAndroid(getUserId) {
    var a = Entrip.getUserId();
    Entrip.showToast(a);
}

document.addEventListener('DOMContentLoaded', () => {
    showAndroid('Hello Android!')
});