// Lấy phần tử của nút
document.getElementById('copyButton').addEventListener('click', function () {
    // Lấy số điện thoại từ phần tử <strong> có id 'phoneNumber'
    var phoneNumber = document.getElementById('phoneNumber').innerText.replace('Liên hệ: ', '');

    // Tạo một trường input tạm để sao chép
    var input = document.createElement('input');
    input.value = phoneNumber;
    document.body.appendChild(input);

    // Chọn và sao chép giá trị trong input
    input.select();
    document.execCommand('copy');

    // Xóa input tạm sau khi sao chép
    document.body.removeChild(input);

    // Hiển thị thông báo
    var notification = document.getElementById('notification');
    notification.style.display = 'block';  // Hiển thị thông báo

    // Ẩn thông báo sau 3 giây
    setTimeout(function () {
        notification.style.display = 'none'; // Ẩn thông báo sau 3 giây
    }, 3000);
});