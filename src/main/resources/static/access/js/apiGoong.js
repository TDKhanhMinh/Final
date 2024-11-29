
    const apiKey = 'KeONrT42qDbhvyFK5oLjywhE0EAcrxeHh0NTznDz';
    const addressInput = document.getElementById('address');
    const suggestionsContainer = document.getElementById('suggestions');
    const cityInput = document.getElementById('city-api');
    const districtInput = document.getElementById('district-api');
    const wardInput = document.getElementById('ward-api');
    let sessionToken = crypto.randomUUID();

    function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
    const later = () => {
    clearTimeout(timeout);
    func(...args);
};
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
};
}

    const debouncedSearch = debounce((query) => {
    if (query.length < 2) {
    suggestionsContainer.style.display = 'none';
    return;
}


    fetch(`https://rsapi.goong.io/Place/AutoComplete?api_key=${apiKey}&input=${encodeURIComponent(query)}&sessiontoken=${sessionToken}`)
    .then(response => response.json())
    .then(data => {
    if (data.status === 'OK') {
    suggestionsContainer.innerHTML = '';
    suggestionsContainer.style.display = 'block';

    data.predictions.forEach(prediction => {
    const div = document.createElement('div');
    div.className = 'suggestion-item';
    div.textContent = prediction.description;
    div.addEventListener('click', () => {
    addressInput.value = prediction.description || '';
    suggestionsContainer.style.display = 'none';

    // Tự động điền các trường địa chỉ từ compound
    if (prediction.compound) {
    cityInput.value = prediction.compound.province || '';
    districtInput.value = prediction.compound.district || '';
    wardInput.value = prediction.compound.commune || '';

}
    console.log("Address updated:", addressInput.value);
});
    suggestionsContainer.appendChild(div);
});
}
})
    .catch(error => console.error('Lỗi:', error));
}, 300);

    addressInput.addEventListener('input', (e) => debouncedSearch(e.target.value));

    document.addEventListener('click', function (e) {
    if (!suggestionsContainer.contains(e.target) && e.target !== addressInput) {
    suggestionsContainer.style.display = 'none';
}
});
