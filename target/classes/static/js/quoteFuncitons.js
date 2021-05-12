function showSimilarQuotes() {
    $.ajax({
        type: 'GET',
        url: "/showSimilar",
        success: function(data) {
            console.log('success', data);
        },
        error: function(exception) {
            alert('Exeption:' + exception);
        }
    });
}