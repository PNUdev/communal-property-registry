const APP_PROPERTIES = new Vue({
    el: '#app_properties',
    data: {
        page: 0,
        q: '',
        status: 'all',
        category: 'all',

        totalPages: 3,
        showModal: false,
        showAttachModal: false,
        hasNext: true,
        hasPrev: false,
        imgUrl: null,
        attachments: [],
        properties: [],
    },

    mounted() {
        this.setParamIfExists("page")
        this.setParamIfExists("q")
        this.setParamIfExists("status")
        this.setParamIfExists("category")

        this.setUrl();
        this.updatePaginationBtnVisibility();
    },

    methods: {
        //ToDo: uncomment updateProperties() and getProperties() on release

        async getProperties() {
            axios.get('/api/property/partial')
                .then(resp => {
                    this.properties = [resp.data];
                    this.totalPages = resp["totalPages"];
                    this.page = resp["number"];
                })
                .catch(error => console.error(error))
        },

        async getPropertyOnMarkerClick(id){
            this.page = 0;
            this.totalPages = 0;
            this.updatePaginationBtnVisibility();

            axios.get(`/api/property/${id}`)
                .then(resp => this.properties = [resp.data])
                .then(error => console.error(error))
        },

        changeFilters(){
            this.setUrl();
            // updateMarkers();
        },

        changePage(e){
            if(e.target.id === "prev-btn" && this.hasPrev){
                this.page--;
            }
            else if(e.target.id === "next-btn" && this.hasNext){
                this.page++;
            }

            this.setUrl();
            this.updatePaginationBtnVisibility();
        },

        dropFilters(){
            this.page = 0;
            this.category = "all";
            this.status = "all";
            this.q = '';
            this.hasPrev = false;
            this.hasNext = true;

            this.setUrl();
            // updateMarkers();
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
                case "USED_BY_COUNCIL": return "Вик. міськвладою"
            }
        },

        searchProperties(){
            this.setUrl();
            // updateMarkers();
        },

        setUrl(){
            let url = "/";

            url += this.page !== 0 ? `?page=${this.page}` : "";
            url += this.q ? `&q=${this.q}` : "";
            url += this.status !== "all" ? `&status=${this.status}` : "";
            url += this.category !== "all" ? `&category=${this.category}` : "";

            url = url.length > 1 ? "?" + url.substring(2) : url;

            history.pushState({}, "", url);

            // this.getProperties();
        },

        setParamIfExists(param){
            let paramValue = new URL(location.href)
                .searchParams.get(param);

            if(paramValue) this[param] = paramValue;
        },

        showAttachmentsModal(attachments){
            this.attachments = attachments;

            if(this.attachments?.length > 0){
                this.showAttachModal = true;
            }
        },

        showImageInModal(imgUrl){
            if(imgUrl) {
                this.imgUrl = imgUrl;
                this.showModal = true;
            }
        },

        updatePaginationBtnVisibility(){
            this.hasPrev = this.page > 0;
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
                .then(resp => this.stats = [resp.data])
                .catch(error => console.error(error))
        },
    }
})