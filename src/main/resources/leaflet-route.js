var map = L.map('map');

map.setView([55.7422, 37.5719], 11);

L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png').addTo(map);

map.on('click', function(e) {
    onMapClick(e);
});

function onMapClick(e) {
    if (loc1 == null) {
        loc1 = new L.marker(e.latlng, {draggable: 'true'});
        loc1.on('dragend', function(event) {
            //отправляем запрос маршрута
        });
        map.addLayer(loc1);
    }
    else if (loc2 == null) {
        loc2 = new L.marker(e.latlng, {draggable: 'true'});
        loc2.on('dragend', function(event) {
            //отправляем запрос марурута
        });
        map.addLayer(loc2);
        //отправляем запрос маршрута
    }
};

var polyline;

function sendPost() {
    if (loc2 != null && loc1 != null) {
        var p1 = loc1.getLatLng(),
            p2 = loc2.getLatLng();
        $.post(
            //куда шлем запрос,
            {l1: p1.lat + ',' + p1.lng, l2: p2.lat + ',' + p2.lng},
            function(data) {
                if (data) {
                    if (polyline) {
                        map.removeLayer(polyline);
                    }
                    var points = data;
                    polyline = new L.polyline(points, {color: 'red'});
                    map.addLayer(polyline);
                    map.fitBounds(polyline.getBounds());
                }
            },
            "json"
        );
    }
}
