<#include "../include/header.ftl">

<main id="wrapper">

    <div id="app_properties" class="properties">

        <transition v-cloak name="fade">
            <div v-if="showModal" @click="showModal=false" class="img-modal">
                <img-modal :src="imgUrl"></img-modal>
            </div>
        </transition>

        <transition v-cloak name="fade">
            <div v-if="showAttachModal" @click="showAttachModal=false" class="attachment-modal" v-model="attachments">
                <div @click.stop class="card mb-3 bg-light border-secondary col-sm-4 mx-auto" v-for="attach in attachments">
                    <h5 class="card-header">{{attach.categoryName}}</h5>
                    <div class="card-body">
                        <p class="card-text">{{attach.note}}</p>
                        <a :href="attach.link" class="btn btn-outline-secondary">
                            Переглянути повну інформацію
                        </a>
                    </div>
                </div>
            </div>
        </transition>

        <div class="properties__header">

            <div class="properties__search">
                <form class="search-form" @submit.prevent="searchProperties">
                    <input class="search-field" v-model.trim="q" type="text" placeholder="Пошук...">
                    <input class="search-btn" type="submit" value="&#9906;">
                </form>

                <a class="properties-report-btn" href="/api/property/report" download>Завантажити звіт</a>
            </div>

            <div class="properties__management">
                <select id="status__filter" v-model="status" @change="changeFilters">
                    <option value="all">Будь-який статус</option>
                    <option value="non_rent">Неорендовані</option>
                    <option value="rent">Орендовані</option>
                    <option value="first_or_second_type_list">I-II типу</option>
                    <option value="privatized">Приватизовані</option>
                    <option value="used_by_council">Вик. міськвладою</option>
                </select>

                <select id="category__filter" v-model="category" @change="changeFilters">
                    <option value="all">Будь-яка категорія</option>
                    <option value="school">Школа</option>
                    <option value="kindergarten">Дитсадок</option>
                </select>

                <p class="drop-filters-btn" @click="dropFilters">&#128473;</p>

                <div class="properties-pagination">
                    <button id="prev-btn" :disabled="!hasPrev" @click="changePage" ></button>
                    <button id="next-btn" :disabled="!hasNext" @click="changePage" ></button>
                </div>
            </div>

        </div>

        <div class="property-items">

            <div class="property" onmouseenter="handlePropertyHoverIn(1)"
                                  onmouseleave="handlePropertyHoverOut(1)">
                <img src="https://www.prostir.ua/wp-content/uploads/2018/03/%D0%9F%D0%9D%D0%A3.jpg"
                     @click="showImageInModal" onerror=this.src="/images/default_img.png">


                <div class="property-data">
                    <h3 class="property__title">ДВНЗ "ПНУ"</h3>
                    <p class="property__area_transferred">3м<sup>2</sup></p>
                    <p class="property__area">1500м<sup>2</sup></p>
                    <p class="property__address">м.Коломия, вул. Миколайчука, 15</p>
                    <p :style="{background:getStatusLabelColor('NON_RENT')}" class="property__status">Неорендовано</p>

                    <ul class="property-description">
                        <li ><span>Власник: </span>ФОП Іванов І.І</li>
                        <li ><span>Балансоутримувач: </span>КНДР</li>
                        <li class="property__endDate"><span>Угода дійсна до: </span>20.01.2025</li>
                    </ul>

                    <button class="property__attachments" href="#" @click="showAttachmentsModal">
                        Переглянути прикріплення 	&#129034;
                    </button>
                    <p class="property__amount">40₴</p>
                </div>
            </div>

            <div class="property" v-model="properties" v-for="prop in properties"
                        @mouseenter="handlePropertyHoverIn(prop.id)" @mouseleave="handlePropertyHoverOut(prop.id)">


                <img title="Open in fullscreen" :src="prop.imageUrl" @click="showImageInModal"
                     onerror=this.src="/images/default_img.png">

                <div class="property-data">
                    <h3 class="property__title">{{prop.name}}</h3>
                    <p class="property__area_transferred">{{prop.areaTransferred}}м<sup>2</sup></p>
                    <p class="property__area">{{prop.area}}м<sup>2</sup></p>
                    <p class="property__address">{{prop.address}}</p>

                    <p :style="{background:getStatusLabelColor(prop.status)}" class="property__status">
                        {{parsePropertyStatus(prop.status)}}
                    </p>

                    <ul class="property-description">
                        <li ><span>Власник: </span>{{prop.owner}}</li>
                        <li ><span>Балансоутримувач: </span>{{prop.balanceHolder}}</li>
                        <li class="property__endDate"><span>Угода дійсна до: </span>{{prop.leaseAgreementEndDate}}</li>
                    </ul>

                    <button class="property__attachments" @click="showAttachmentsModal(prop.id)">
                        Переглянути прикріплення 	&#129034;
                    </button>
                    <p class="property__amount">{{prop.amountOfRent}}₴</p>
                </div>
            </div>

            <!-- get from server -->
        </div>

    </div>

    <section class="map-section">
        <div id="map"></div>

        <div id="app_stats" class="stats">

            <table class="table table-striped table-hover">
                <thead>
                <tr>
                    <th scope="col">Категорія</th>
                    <th scope="col">Загалом</th>
                    <th scope="col">Орендовані</th>
                    <th scope="col">Неорендовані</th>
                    <th scope="col">I-II типу</th>
                    <th scope="col">Приватиз.</th>
                    <th scope="col">Вик. міськвладою</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th scope="row">Школа</th>
                    <td>15</td> <td>3</td> <td>4</td>
                    <td>5</td> <td>1</td> <td>2</td>
                </tr>
                <tr>
                    <th scope="row">Школа</th>
                    <td>15</td> <td>3</td> <td>4</td>
                    <td>5</td> <td>1</td> <td>2</td>
                </tr>
                <tr>
                    <th scope="row">Школа</th>
                    <td>15</td> <td>3</td> <td>4</td>
                    <td>5</td> <td>1</td> <td>2</td>
                </tr>
                <tr>
                    <th scope="row">Школа</th>
                    <td>15</td> <td>3</td> <td>4</td>
                    <td>5</td> <td>1</td> <td>2</td>
                </tr>
                <tr>
                    <th scope="row">Школа</th>
                    <td>15</td> <td>3</td> <td>4</td>
                    <td>5</td> <td>1</td> <td>2</td>
                </tr>

                <tr>
                    <th scope="row">Школа</th>
                    <td>15</td> <td>3</td> <td>4</td>
                    <td>5</td> <td>1</td> <td>2</td>
                </tr>

                <tr class="stats__item" v-for="stat in stats" v-cloak>
                    <th scope="row">{{stat.category}}</th>
                    <td>{{stat.totalNumber}}</td>
                    <td>{{stat.numberOfRented}}</td>
                    <td>{{stat.numberOfNonRented}}</td>
                    <td>{{stat.numberOfListed}}</td>
                    <td>{{stat.numberOfPrivatized}}</td>
                    <td>{{stat.numberOfUsedByCityCouncil}}</td>
                </tr>

                </tbody>
            </table>

        </div>
    </section>

</main>

<#include "../include/footer.ftl">