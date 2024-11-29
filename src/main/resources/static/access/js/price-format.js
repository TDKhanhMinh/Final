function formatPrice(price) {
    return price.toLocaleString('vi-VN') + ' VNĐ';
}

function cleanAndParsePrice(priceElement) {
    // Clean the text by removing non-numeric characters except periods (.) and hyphens (-)
    const priceText = priceElement.textContent.replace(/[^\d.,-]/g, '');
    // Remove any commas used as thousand separators
    const cleanPrice = priceText.replace(/,/g, '');
    // Parse the cleaned string into a float
    const parsedPrice = parseFloat(cleanPrice);
    // Return NaN if the parsed value is invalid, so we can handle it later
    return isNaN(parsedPrice) ? 0 : parsedPrice;
}

// Apply formatting to all original and sale prices on page load
document.addEventListener('DOMContentLoaded', function () {
    const salePrices = document.querySelectorAll('.priceSale');
    salePrices.forEach(priceElement => {
        const price = cleanAndParsePrice(priceElement);
        priceElement.textContent = formatPrice(price);
    });
});

