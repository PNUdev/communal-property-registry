const APP_PROPERTIES = new Vue({
    el: '#app_properties',
    data: {
        page: 1,
        q: '',
        status: 'all',
        category: 'all',

        totalPages: 1,
        showFilters: true,
        showModal: false,
        showAttachModal: false,
        hasNext: false,
        hasPrev: false,
        isLoaded: true,
        imgUrl: null,
        defaultImgUrl: "/images/default_img.png",
        url: "",

        attachments: [],
        properties: [""],
        categories: []
    },

    created(){
        this.insertPropertyPlaceholder();
    },

    mounted() {
        this.setParamIfExists("page")
        this.setParamIfExists("q")
        this.setParamIfExists("status")
        this.setParamIfExists("category")

        this.setUrl();
        this.getCategories();
    },

    methods: {

        async getCategories(){
            axios.get('/api/categories-by-purpose')
                .then(resp => this.categories = resp.data["categoriesByPurpose"])
                .catch(error => {
                    console.error("CATEGORIES-BY-PURPOSE FAILED TO LOAD\n" + error);
                })
        },

        async getProperties() {
            this.isLoaded = false;
            axios.get(`/api/properties${this.url}`)
                .then(resp => {
                    this.properties = resp.data["content"];
                    this.totalPages = resp.data["totalPages"];

                    this.showFilters = true;
                    this.isLoaded = true;
                    this.updatePaginationBtnVisibility();
                })
                .catch(error => {
                    console.error("PROPERTIES FAILED TO LOAD\n" + error);
                })
        },

        async getPropertyOnMarkerClick(id){
            this.page = 1;
            this.totalPages = 1;
            this.isLoaded = false;
            this.updatePaginationBtnVisibility();

            if(window.innerWidth <= 1000){
                switchMapPropertiesView(document.querySelector(".switch-btn"));
            }

            axios.get(`/api/properties/${id}`)
                .then(resp =>{
                    this.properties = [resp.data];
                    this.showFilters = false;
                    this.isLoaded = true;
                })
                .catch(error => {
                    console.error(`PROPERTY WITH ID=${id} FAILED TO LOAD\n ${error}`);
                })
        },

        changeFilters(){
            this.page = 1;
            this.setUrl();
            updateMarkers();
        },

        changePage(e){
            if(e.target.id === "prev-btn" && this.hasPrev){
                this.page--;
                this.$el.lastChild.scrollTop = 0;
            }
            else if(e.target.id === "next-btn" && this.hasNext){
                this.page++;
                this.$el.lastChild.scrollTop = 0;
            }

            this.setUrl();
            this.updatePaginationBtnVisibility();
        },

        insertPropertyPlaceholder(){
            let container = document.querySelector(".property-items_loading");
            let template = `
                 <div class="property property_loading">
                    <div class="property__img_loading"></div>
                    <div class="property-data property-data_loading"><p></p><p></p><p></p></div>
                 </div>`

            container.insertAdjacentHTML("afterbegin", template.repeat(5));
        },

        getStatusLabelColor(status){
            return STATUS_COLORS[status];
        },

        parsePropertyStatus(status){
             switch(status){
                case "RENT" : return "Орендовано";
                case "NON_RENT": return "Неорендовано";
                case "FIRST_OR_SECOND_TYPE_LIST": return "I-II типу";
                case "PRIVATIZED": return "Приватизовано";
                case "USED_BY_CITY_COUNCIL": return "Вик. міськвладою"
            }
        },

        searchProperties(event){
            this.page = 1;
            this.q = event.target[0].value.trim();

            this.setUrl();
            this.updatePaginationBtnVisibility();
            updateMarkers();
        },

        setUrl(){
            let url = "";

            url += this.page !== 1 ? `?page=${this.page}` : "";
            url += this.q ? `&q=${this.q}` : "";
            url += this.status !== "all" ? `&status=${this.status}` : "";
            url += this.category !== "all" ? `&category=${this.category}` : "";

            url = url.length > 1 ? "?" + url.substring(1) : url;
            this.url = url;

            if(url === ""){
                history.pushState({}, "", window.location.pathname);
            }
            else  history.pushState({}, "", this.url);

            this.getProperties();
        },

        setParamIfExists(param){
            let paramValue = new URL(location.href)
                .searchParams.get(param);

            if(paramValue) this[param] = paramValue;
        },

        showAll(){
            this.page = 1;
            this.category = "all";
            this.status = "all";
            this.q = '';
            this.hasPrev = false;
            this.hasNext = true;

            this.setUrl();
            this.$el.lastChild.scrollTop = 0
            updateMarkers();
        },

        showAttachmentsModal(attachments){
            this.attachments = attachments;

            if(this.attachments && this.attachments.length > 0){
                this.showAttachModal = true;
            }
        },

        showImageInModal(event){
            let isLoaded = event.target.complete;
            let isDefault = event.target.src.endsWith(this.defaultImgUrl);

            if(isLoaded && !isDefault) {
                this.imgUrl = event.target.src;
                this.showModal = true;
            }
        },

        updatePaginationBtnVisibility(){
            this.hasPrev = this.page > 1;
            this.hasNext = this.page < this.totalPages;
        },

    }
})

const APP_STATS = new Vue({
    el: "#app_stats",

    data: {
        stats: []
    },

    mounted() {
        this.getStatistics();
    },

    methods: {
        async getStatistics() {
            axios.get('/api/statistics')
                .then(resp => this.stats = resp.data["propertyStatistics"])
                .catch(error => {
                    console.error("STATISTICS FAILED TO LOAD\n" + error);
                })
        },
    }
})

function switchMapPropertiesView(button){
    const properties = document.querySelector(".properties");
    const mapAndStats = document.querySelector(".map-section");

    button.innerHTML = button.innerHTML === "Відкрити карту" ? "&#8592; Повернутись" : "Відкрити карту";
    properties.style.display = getComputedStyle(properties).display === "none" ? "flex" : "none";
    mapAndStats.style.display = getComputedStyle(mapAndStats).display === "none" ? "flex" : "none";

    //map display settings drop after display:none, so this re-renders it properly
    map.invalidateSize();
}
