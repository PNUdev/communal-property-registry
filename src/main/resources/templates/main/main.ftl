<#include "../include/header.ftl">

<main id="wrapper">

    <div id="app_properties" class="properties">

        <transition v-cloak name="fade">
            <div v-if="showModal" @click="showModal=false" class="img-modal">
                <button @click="showModal=false" class="fas fa-times modal-close img-modal-close"></button>
                <img :src="imgUrl" alt="NOT FOUND">
            </div>
        </transition>

        <transition v-cloak name="fade">
            <div v-if="showAttachModal" @click="showAttachModal=false" class="attachment-modal" v-model="attachments">

                <div class="attachment-modal-items">
                    <button @click="showAttachModal=false"
                            class="fas fa-times modal-close attachments-modal-close"></button>

                    <div @click.stop class="card mb-3 bg-light border-secondary col-sm-4 mx-auto"
                         v-for="attach in attachments" v-if="attach != null">

                        <h5 class="card-header">{{attach.categoryName}}</h5>
                        <div class="card-body">
                            <p class="card-text">{{attach.note}}</p>
                            <a :href="attach.link" class="btn btn-outline-secondary">
                                Переглянути повну інформацію
                            </a>
                        </div>
                    </div>
                </div>

            </div>
        </transition>

        <div class="properties__header">

            <div class="properties__search">
                <form class="search-form" @submit.prevent="searchProperties">
                    <input class="search-field" :value="q" type="text" placeholder="Пошук...">
                    <button class="search-btn fab fa-sistrix" type="submit"></button>
                </form>

                <a class="properties-report-btn" :href="'/api/properties/report' + url" download>
                        Завантажити звіт
                </a>
            </div>
            

            <div class="properties__management">

                <select :disabled="!showFilters" id="status__filter" v-model="status" @change="changeFilters">
                    <option value="all">Будь-який статус</option>
                    <option value="NON_RENT">Неорендовані</option>
                    <option value="RENT">Орендовані</option>
                    <option value="FIRST_OR_SECOND_TYPE_LIST">I-II типу</option>
                    <option value="PRIVATIZED">Приватизовані</option>
                    <option value="USED_BY_CITY_COUNCIL">Вик. міськвладою</option>
                </select>

                <select :disabled="!showFilters" id="category__filter" v-model="category" @change="changeFilters">
                    <option value="all">Будь-яка категорія</option>

                    <option v-for="cat in categories" :value="cat.id" v-model="categories">
                        {{cat.name}}
                    </option>
                </select>

                <p v-if="!(category == status && q == '' && properties.length != 1)" class="show-all-btn"
                   @click="showAll">Показати всі</p>

                <div class="properties-pagination">
                    <button id="prev-btn" class="fas fa-chevron-left" :disabled="!hasPrev" @click="changePage"></button>
                    <div v-cloak class="properties-pagination__info">{{page}} із {{totalPages > 0 ? totalPages : 1}}
                    </div>
                    <button id="next-btn" class="fas fa-chevron-right" :disabled="!hasNext"
                            @click="changePage"></button>
                </div>
            </div>

        </div>

        <div class="property-items" v-cloak>
            <div v-if="!isLoaded" class="property-items_loading"></div>

            <div v-else class="property" v-model="properties" v-for="prop in properties"
                 @mouseenter="handlePropertyHoverIn(prop.id)" @mouseleave="handlePropertyHoverOut(prop.id)">

                <img title="Open in fullscreen" @click="showImageInModal"
                     :src="prop.imageUrl ? prop.imageUrl : defaultImgUrl"
                     @error="$event.target.src = defaultImgUrl">

                <div class="property-data">
                    <div>
                        <h3 class="property__title">{{prop.name}}</h3>

                        <div class="property-area-main">
                            <p class="property__area_transferred" v-if="prop.areaTransferred">
                                {{prop.areaTransferred}}м<sup>2</sup>
                            </p>

                            <p class="property__area">{{prop.area}}м<sup>2</sup></p>
                        </div>
                    </div>

                    <p class="property__address">{{prop.address}}</p>

                    <p :style="{background:getStatusLabelColor(prop.propertyStatus)}" class="property__status">
                        {{parsePropertyStatus(prop.propertyStatus)}}
                    </p>

                    <ul class="property-description">
                        <li v-if="prop.owner"><span>Власник: </span>{{prop.owner}}</li>

                        <li v-if="prop.balanceHolder">
                            <span>Балансоутримувач: </span>{{prop.balanceHolder}}
                        </li>

                        <li v-if="prop.leaseAgreementEndDate" class="property__endDate">
                            <span>Угода дійсна до: </span>{{prop.leaseAgreementEndDate}}
                        </li>
                    </ul>

                    <div class="property__footer">
                        <button :class="[(prop.attachments && prop.attachments.length > 0) ?
                                            '': 'property__attach_disabled', 'property__attach']"
                                @click="showAttachmentsModal(prop.attachments)">

                            Переглянути прикріплення <i class="fas fa-chevron-right"></i>
                        </button>

                        <p v-if="prop.amountOfRent" class="property__amount">{{prop.amountOfRent}}₴</p>
                    </div>
                </div>

            </div>

            <p class="properties__notfound" v-if="properties.length == 0">
                Приміщень не знайдено . . .
            </p>
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
                <tr class="stats__item" v-for="stat in stats" v-model="stats" v-cloak>
                    <th scope="row">{{stat.category}}</th>
                    <td>{{stat.totalNumber}}</td>
                    <td>{{stat.numberOfRented}}</td>
                    <td>{{stat.numberOfNonRented}}</td>
                    <td>{{stat.numberOfFirstOrSecondType}}</td>
                    <td>{{stat.numberOfPrivatized}}</td>
                    <td>{{stat.numberOfUsedByCityCouncil}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </section>

</main>

<#include "../include/footer.ftl">