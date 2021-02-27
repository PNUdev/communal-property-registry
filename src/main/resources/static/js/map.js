let map = L.map('map').setView([49.25589, 24.90972], 7)

//init map
L.tileLayer('https://api.maptiler.com/maps/streets/{z}/{x}/{y}.png?key=FLZjrggiEUkOsMhDShR0', {
    attribution: `<a href="https://www.maptiler.com/copyright/" target="_blank">&copy; MapTiler</a>
                  <a href="https://www.openstreetmap.org/copyright" target="_blank" >&copy; OpenStreetMap contributors</a> `
}).addTo(map);


//Marker color depending on its status
const STATUS_COLORS = {
    NON_RENT: "rgb(173,207,103)",
    RENT: "rgb(227,98,98)",
    FIRST_OR_SECOND_TYPE_LIST: "rgb(99,167,233)",
    PRIVATIZED: "rgb(227,149,84)",
    USER_BY_COUNCIL: "rgb(174,152,229)"
}

//marker icon
class Icon {
    constructor(color) {
        this.color = color;
        return this.getInstance();
    }

    getInstance() {
        return L.icon({
            iconUrl: `data:image/svg+xml;charset=utf8,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 64 64'
                aria-labelledby='title' aria-describedby='desc' role='img'
                xmlns:xlink='http://www.w3.org/1999/xlink'%3E%3Ctitle%3EPin%3C/title%3E%3Cdesc%3EA
                solid styled icon from Orion Icon Library.%3C/desc%3E%3Cpath data-name='layer1'
                d='M32 2a20 20 0 0 0-20 20c0 18 20 40 20 40s20-22 20-40A20 20 0 0 0 32
                2zm0 28a8 8 0 1 1 8-8 8 8 0 0 1-8 8z' fill='${this.color}' %3E%3C/path%3E%3C/svg%3E`,
            iconSize: [40, 45],
            iconAnchor: [20, 43],
            popupAnchor: [0, -36]
        });
    }
}


//ToDo: remove static markers when server data will arrive
//represents json data from server !!!

// ONLY FOR DEMO
let markers = [
    {
        id: '1',
        coords: { lat: 48.9215, lng: 24.70972 },
        status: "NON_RENT"
    },

    {
        id: '2',
        coords: { lat: 49.55589, lng: 25.60556 },
        status: "USER_BY_COUNCIL"
    },

    {
        id: '3',
        coords: { lat: 49.06254, lng: 25.38798 },
        status: "PRIVATIZED"
    }
]
//END DEMO


let markerLayers = [];
// getMarkers();
markers.forEach(p => addMarker(p))

//update markers, remove old, add new
function updateMarkers(){
    while(markerLayers.length > 0) {
        map.removeLayer(markerLayers.pop());
    }
    getMarkers();
}

//get all markers
function getMarkers(){
    axios.get('/api/property/map-locations')
        .then(resp => {
            resp.data.forEach(d=>{
                addMarker({
                    id: d.id,
                    coords: {lat: d.lat, lng: d.lon},
                    status: d.propertyStatus
                })
            })
        })
        .catch(error => console.error(error))
}

//Create new marker
function addMarker(marker) {
    let icon = new Icon(STATUS_COLORS[marker.status]);

    let newMarker = Object.defineProperty(L.marker(marker.coords, { icon: icon }),
        'id', {value:marker.id})
        .addTo(map)

    newMarker.on('click', handleMarkerClick)
    markerLayers.push(newMarker);
}

//display property when clicking marker
function handleMarkerClick(e) {
    APP_PROPERTIES.getPropertyOnMarkerClick(e.target.id)
    map.setView(e.target.getLatLng(), 7)
}

let oldPosition;
function handlePropertyHoverIn(propertyId) {
    let markerStyles;

    try {
        markerStyles = markerLayers
            .find(m => m.id === propertyId + '')
            ._icon.style;

    }  catch(e){ console.error("APPROPRIATE MARKER WAS NOT FOUND"); return}

    oldPosition = markerStyles.transform;

    let newPosition = oldPosition
        .substring(12, oldPosition.length - 1)
        .split(",")
        .map(p => parseInt(p));

    newPosition[0] -= 3; newPosition[1] -= 6
    newPosition = newPosition.map(p => p + "px").join(", ")

    markerStyles.transition = ".2s linear";
    markerStyles.filter = "brightness(1.1) drop-shadow(0 0 20px grey)";
    markerStyles.transform = `translate3d(${newPosition}) scale(1.2)`;
}

function handlePropertyHoverOut(propertyId) {
    let markerStyles;

    try{
        markerStyles = markerLayers
            .find(m => m.id === propertyId+'')
            ._icon.style;

    } catch(e){ console.error("APPROPRIATE MARKER WAS NOT FOUND"); return}

    markerStyles.filter = "brightness(1)";
    markerStyles.transform = oldPosition;
}