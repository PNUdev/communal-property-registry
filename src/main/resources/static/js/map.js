//init map
const KOLOMYIA_MAP_LOCATION = {
    lat: 48.53,
    lon: 25.05,
};

let map = L.map('map').setView([KOLOMYIA_MAP_LOCATION.lat, KOLOMYIA_MAP_LOCATION.lon], 13)

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
    USED_BY_CITY_COUNCIL: "rgb(174,152,229)"
}

//marker icon
class Icon {

    constructor(color) {
        return L.divIcon({
            html: `<i class="fas fa-map-marker-alt" style="color:${color}"></i>`,
            iconAnchor: [20, 43],
            className: "marker__icon"
        });
    }

}

let markerLayers = [];
getMarkers();

//update markers, remove old, add new
function updateMarkers() {
    while (markerLayers.length > 0) {
        map.removeLayer(markerLayers.pop());
    }

    getMarkers();
}

//get all markers
async function getMarkers() {
    axios.get(`/api/properties/map-locations` + location.search)
        .then(resp => {
            resp.data["mapLocations"].forEach(propertyLocationData => {
                addMarker({
                    id: propertyLocationData.propertyId,

                    coords: {
                        lat: propertyLocationData.lat,
                        lng: propertyLocationData.lon
                    },

                    status: propertyLocationData.propertyStatus
                })
            })
        })
        .catch(error => {
            console.error("MARKER LOCATIONS FAILED TO LOAD\n" + error);
        })
}

//Create new marker
function addMarker(marker) {
    let icon = new Icon(STATUS_COLORS[marker.status]);

    let newMarker = Object.defineProperty(L.marker(marker.coords, {icon: icon}),
        'id', {value: marker.id})
        .addTo(map)

    newMarker.addEventListener("click", handleMarkerClick, {once: true});
    markerLayers.push(newMarker);
}

//display property when clicking marker
function handleMarkerClick(e) {
    if (e.originalEvent.isTrusted) {
        APP_PROPERTIES.getPropertyOnMarkerClick(e.target.id)
    }
}

//Highlight marker on property hover
let oldPosition;

function handlePropertyHoverIn(propertyId) {
    let markerStyles;

    try {
        markerStyles = markerLayers
            .find(marker => marker.id === propertyId)
            ._icon.style;

    } catch (error) {
        console.error("APPROPRIATE MARKER WAS NOT FOUND\n" + error);
        return;
    }

    oldPosition = markerStyles.transform;

    //When marker scales it shifts from the initial point, so this case is handled here
    let newPosition = oldPosition
        .substring(12, oldPosition.length - 1)
        .split(",")
        .map(point => parseInt(point));

    newPosition[0] -= 3;
    newPosition[1] -= 6;
    newPosition = newPosition.map(point => point + "px").join(", ")

    markerStyles.transition = ".2s linear";
    markerStyles.filter = "brightness(1.1) drop-shadow(0 0 20px grey)";
    markerStyles.transform = `translate3d(${newPosition}) scale(1.2)`;
}

function handlePropertyHoverOut(propertyId) {
    let markerStyles;

    try {
        markerStyles = markerLayers
            .find(marker => marker.id === propertyId)
            ._icon.style;

    } catch (error) {
        console.error("APPROPRIATE MARKER WAS NOT FOUND\n" + error);
        return;
    }

    markerStyles.filter = "brightness(1)";
    markerStyles.transform = oldPosition;
}