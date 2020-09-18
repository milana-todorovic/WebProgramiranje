Vue.component("edit-apartment", {
    data: function () {
        return {
            apartment: null,
            comments: [],
            images: [],
            name: { value: null, state: null, error: null },
            numberOfRooms: { value: null, state: null, error: null },
            numberOfGuests: { value: null, state: null, error: null },
            pricePerNight: { value: null, state: null, error: null },
            checkInTime: { value: "14:00:00", state: true, error: null },
            checkOutTime: { value: "10:00:00", state: true, error: null },
            latitude: { value: null, state: null, error: null },
            longitude: { value: null, state: null, error: null },
            street: { value: null, state: null, error: null },
            number: { value: null, state: null, error: null },
            city: { value: null, state: null, error: null },
            postalCode: { value: null, state: null, error: null },
            country: { value: null, state: null, error: null },
            status: null,
            type: null,
            datesForRenting: [],
            amenities: [],
            allAmenities: [],
            globalAlert: { show: false, text: null },
            imageAlert: { show: false, text: null },
            commentAlert: { show: false, text: null },
            imageToUpload: []
        }
    },
    created() {
        this.fetchData();
    },
    watch: {
        '$route': 'fetchData'
    },
    computed: {
        formValid: function () {
            return this.name.state && this.numberOfRooms.state && this.numberOfGuests.state
                && this.pricePerNight.state && this.checkInTime.state && this.checkOutTime.state
                && this.latitude.state && this.longitude.state && this.city.state && this.country.state
                && this.number.state && this.street.state;
        }
    },
    methods: {
        fetchAmenities() {
            axios.get("/WebApartmani/rest/amenities").then(
                response => this.amenitiesRead(response.data)).catch(
                    error => this.setGlobalAlert("Do\u0161lo je do gre\u0161ke pri u\u010Ditavanju sadr\u017Eaja apartmana."));
        },
        fetchData() {
            this.apartment = null;
            axios.get("/WebApartmani/rest/apartments/" + this.$route.params.id).then(
                response => this.apartmentLoaded(response.data)
            ).catch(error => { this.setGlobalAlert(error.response.data) });
            this.fetchAmenities();
        },
        apartmentLoaded(apartment) {
            if (apartment.id == this.$route.params.id) {
                this.apartment = apartment;
                let fixedDates = [];
                for (date of apartment.datesForRenting) {
                    let realDate = new Date(date);
                    fixedDates.push(new Date(realDate.getTime() + realDate.getTimezoneOffset() * 60 * 1000));
                }
                apartment.datesForRenting = fixedDates;
                this.reset();
                axios.get("/WebApartmani/rest/comments", {
                    params: { apartment: apartment.id }
                }).then(response => this.commentsLoaded(apartment, response.data)).catch(
                    error => { if (this.$route.params.id == apartment.id) this.setCommentAlert("Nije uspelo u\u010Ditavanje komentara."); console.log(error) });
            }
        },
        commentsLoaded(apartment, comments) {
            if (apartment.id == this.$route.params.id) {
                this.comments = comments;
                axios.get("/WebApartmani/rest/apartments/" + apartment.id + "/images").then(response => this.allLoaded(apartment, response.data)).catch(
                    error => { if (this.$route.params.id == apartment.id) this.setImageAlert("Nije uspelo u\u010Ditavanje slika."); console.log(error) });
            }
        },
        allLoaded(apartment, images) {
            if (apartment.id == this.$route.params.id) {
                this.images = images;
            }
        },
        amenitiesRead(amenities) {
            this.allAmenities = [];
            for (amenity of amenities) {
                this.allAmenities.push({ value: amenity, text: amenity.name });
            }
        },
        submit(event) {
            event.preventDefault();
            this.validateName();
            this.validateNumberOfGuests();
            this.validateNumberOfRooms();
            this.validatePricePerNight();
            this.validateCheckInTime();
            this.validateCheckOutTime();
            this.validateStreet();
            this.validateNumber();
            this.validateCity();
            this.validateCountry();
            this.validateLatitude();
            this.validateLongitude();
            if (!this.formValid)
                return;
            let dates = [];
            for (date of this.datesForRenting)
                dates.push(date.getTime() - date.getTimezoneOffset() * 60 * 1000);
            axios.put("/WebApartmani/rest/apartments/" + this.apartment.id,
                {
                    name: this.name.value,
                    apartmentType: this.type,
                    numberOfRooms: this.numberOfRooms.value,
                    numberOfGuests: this.numberOfGuests.value,
                    pricePerNight: this.pricePerNight.value,
                    checkInTime: this.checkInTime.value,
                    checkOutTime: this.checkOutTime.value,
                    datesForRenting: dates,
                    amenities: this.amenities,
                    location: { latitude: 19, longitude: 45 },
                    id: this.apartment.id,
                    status: this.status,
                    host: this.apartment.host,
                    location: {
                        latitude: this.latitude.value,
                        longitude: this.longitude.value,
                        address:{
                            street: this.street.value,
                            number: this.number.value,
                            city: this.city.value,
                            postalCode: this.postalCode.value,
                            country: this.country.value
                        }
                    }
                }
            ).then(response => { this.setGlobalAlert("Izmena je uspe\u0161no izvr\u0161ena."); }).catch(
                error => this.setGlobalAlert("Izmena nije uspela: " + error.response.data));
        },
        reset() {
            this.status = this.apartment.status;
            this.name = { value: this.apartment.name, state: true, error: null };
            this.numberOfRooms = { value: this.apartment.numberOfRooms, state: true, error: null };
            this.numberOfGuests = { value: this.apartment.numberOfGuests, state: true, error: null };
            this.pricePerNight = { value: this.apartment.pricePerNight, state: true, error: null };
            this.checkInTime = { value: this.apartment.checkInTime, state: true, error: null };
            this.checkOutTime = { value: this.apartment.checkOutTime, state: true, error: null };
            this.longitude = { value: this.apartment.location.longitude, state: true, error: null };
            this.latitude = { value: this.apartment.location.latitude, state: true, error: null };
            this.street = { value: this.apartment.location.address.street, state: true, error: null };
            this.number = { value: this.apartment.location.address.number, state: true, error: null };
            this.city = { value: this.apartment.location.address.city, state: true, error: null };
            this.country = { value: this.apartment.location.address.country, state: true, error: null };
            this.postalCode = { value: this.apartment.location.address.postalCode, state: true, error: null };
            this.type = this.apartment.apartmentType;
            this.datesForRenting = this.apartment.datesForRenting;
            this.amenities = this.apartment.amenities;
            this.imageToUpload = null;
            this.globalAlert = { show: false, text: null };
            this.imageAlert = { show: false, text: null };
            this.commentAlert = { show: false, text: null };
        },
        validateName() {
            if (!this.name.value) {
                this.name.error = "Naziv je obavezan."
                this.name.state = false;
                return;
            }
            this.name.state = true;
        },
        validateNumberOfRooms() {
            if (!this.numberOfRooms.value) {
                this.numberOfRooms.error = "Broj soba je obavezan."
                this.numberOfRooms.state = false;
                return;
            }
            if (! /^[1-9][0-9]*$/.test(this.numberOfRooms.value)) {
                this.numberOfRooms.error = "Broj soba mora biti pozitivan broj ve\u0107i od 1."
                this.numberOfRooms.state = false;
                return;
            }
            this.numberOfRooms.state = true;
        },
        validateNumberOfGuests() {
            if (!this.numberOfGuests.value) {
                this.numberOfGuests.error = "Broj gostiju je obavezan."
                this.numberOfGuests.state = false;
                return;
            }
            if (! /^[1-9][0-9]*$/.test(this.numberOfGuests.value)) {
                this.numberOfGuests.error = "Broj gostiju mora biti pozitivan broj ve\u0107i od 1."
                this.numberOfGuests.state = false;
                return;
            }
            this.numberOfGuests.state = true;
        },
        validateNumber() {
            if (this.number.value && ! /^[1-9][0-9]*$/.test(this.number.value)) {
                this.number.error = "Broj u ulici mora biti pozitivan broj ve\u0107i od 1."
                this.number.state = false;
                return;
            }
            this.number.state = true;
        },
        validateLatitude() {
            if (!this.latitude.value) {
                this.latitude.error = "Geografska \u0161irina je obavezna."
                this.latitude.state = false;
                return;
            }
            this.latitude.state = true;
        },
        validateLongitude() {
            if (!this.longitude.value) {
                this.longitude.error = "Geografska du\u017Eina je obavezna."
                this.longitude.state = false;
                return;
            }
            this.longitude.state = true;
        },
        validateStreet() {
            if (!this.street.value) {
                this.street.error = "Ulica je obavezna."
                this.street.state = false;
                return;
            }
            this.street.state = true;
        },
        validateCity() {
            if (!this.city.value) {
                this.city.error = "Grad je obavezan."
                this.city.state = false;
                return;
            }
            this.city.state = true;
        },
        validateCountry() {
            if (!this.country.value) {
                this.country.error = "Dr\u017Eava je obavezna."
                this.country.state = false;
                return;
            }
            this.country.state = true;
        },
        validatePricePerNight() {
            if (!this.pricePerNight.value) {
                this.pricePerNight.error = "Cena je obavezna."
                this.pricePerNight.state = false;
                return;
            }
            if (! /^\d+(\.\d+)?$/.test(this.pricePerNight.value)) {
                this.pricePerNight.error = "Cena mora biti pozitivan broj."
                this.pricePerNight.state = false;
                return;
            }
            this.pricePerNight.state = true;
        },
        validateCheckInTime() {
            if (!this.checkInTime.value) {
                this.checkInTime.error = "Vreme prijave je obavezno."
                this.checkInTime.state = false;
                return;
            }
            this.checkInTime.state = true;
        },
        validateCheckOutTime() {
            if (!this.checkOutTime.value) {
                this.checkOutTime.error = "Vreme odjave je obavezno."
                this.checkOutTime.state = false;
                return;
            }
            this.checkOutTime.state = true;
        },
        setGlobalAlert(text) {
            this.globalAlert.text = text;
            this.globalAlert.show = true;
        },
        setImageAlert(text) {
            this.imageAlert.text = text;
            this.imageAlert.show = true;
        },
        setCommentAlert(text) {
            this.commentAlert.text = text;
            this.commentAlert.show = true;
        },
        imageSubmit(event) {
            event.preventDefault();
            if (!(this.imageToUpload.length > 0)) {
                this.setImageAlert("Nije izabrana slika za dodavanje.");
                return;
            }

            var reader = new FileReader();
            reader.readAsDataURL(this.imageToUpload[0]);
            reader.onload =
                () => {
                    axios({
                        method: 'POST',
                        url: "/WebApartmani/rest/apartments/" + this.apartment.id + "/images",
                        data: JSON.stringify(reader.result),
                        headers: { 'Content-Type': 'application/json; charset=utf-8' }
                    }).then(
                        response => { this.images.push(response.data); this.setImageAlert("Slika je uspe\u0161no dodata"); this.imageToUpload = []; }
                    ).catch(
                        error => this.setImageAlert("Nije uspelo dodavanje slike: " + error.response.data)
                    )
                }
        },
        change: function (event) {
            this.imageToUpload = event.target.files;
        },
        deleteImage(image) {
            axios.delete("/WebApartmani/rest/apartments/" + this.apartment.id + "/images/" + image.id).then(
                response => this.imageDeleted(image)
            ).catch(
                error => this.setImageAlert("Nije uspelo brisanje slike: " + error.response.data)
            )
        },
        imageDeleted(image) {
            let imageList = [];
            for (im of this.images)
                if (im.id !== image.id)
                    imageList.push(im);
            this.images = imageList;
            this.setImageAlert("Slika je uspe\u0161no obrisana.")
        },
        hide(comment) {
            axios({
                method: 'put',
                url: '/WebApartmani/rest/comments/' + comment.id + "/showing",
                data: false,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(
                data => comment.showing = false).catch(error => this.setCommentAlert("Nije uspelo sakrivanje komentara: " + 
                error.response.data));
        },
        show(comment) {
            axios({
                method: 'put',
                url: '/WebApartmani/rest/comments/' + comment.id + "/showing",
                data: true,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(
                data => comment.showing = true).catch(error => this.setCommentAlert("Nije uspelo prikazivanje komentara: " + 
                error.response.data));
        }
    },
    template: `
      <b-container class="p-5" fluid>
      <b-row>
      <b-col class="border rounded m-2 pt-2">
    <b-alert
      v-model="globalAlert.show"
      dismissible>
      {{ globalAlert.text }}
    </b-alert>   
  
    <h3>Izmena op\u0161tih podataka</h3>
    <b-form v-on:submit="submit" v-on:reset="reset">        
        <b-form-group
          id="input-group-1"
          label="Naziv:"
          label-for="name"
        >
          <b-form-input
            :state="name.state"
            id="name"
            v-model="name.value"
            type="text"
            placeholder="Unesite naziv"
            v-on:blur="validateName"
          ></b-form-input>
          <b-form-invalid-feedback :state="name.state">
          {{name.error}}
            </b-form-invalid-feedback>
        </b-form-group>
  
        <b-form-group label="Tip apartmana">
        <b-form-radio v-model="type" name="type" value="Soba">Soba</b-form-radio>
        <b-form-radio class="mt-1" v-model="type" name="type" value="Ceo apartman">Ceo apartman</b-form-radio>
        </b-form-group>

        <b-form-group label="Status apartmana">
        <b-form-radio v-model="status" name="statuc" value="Aktivan">Aktivan</b-form-radio>
        <b-form-radio class="mt-1" v-model="status" name="statuc" value="Neaktivan">Neaktivan</b-form-radio>
        </b-form-group>
  
        <b-form-group
          label="Broj soba:"
        >
          <b-form-input
            :state="numberOfRooms.state"
            v-model="numberOfRooms.value"
            type="text"
            placeholder="Unesite broj soba"
            v-on:blur="validateNumberOfRooms"
          ></b-form-input>
          <b-form-invalid-feedback :state="numberOfRooms.state">
          {{numberOfRooms.error}}
            </b-form-invalid-feedback>
        </b-form-group>
  
        <b-form-group
          label="Broj gostiju:"
        >
          <b-form-input
            :state="numberOfGuests.state"
            v-model="numberOfGuests.value"
            type="text"
            placeholder="Unesite broj gostiju"
            v-on:blur="validateNumberOfGuests"
          ></b-form-input>
          <b-form-invalid-feedback :state="numberOfGuests.state">
          {{numberOfGuests.error}}
            </b-form-invalid-feedback>
        </b-form-group>

        <b-form-group
          label="Geografska du\u017Eina:"
        >
          <b-form-input
            :state="latitude.state"
            v-model="latitude.value"
            type="text"
            placeholder="Unesite geografsku du\u017Einu"
            v-on:blur="validateLatitude"
          ></b-form-input>
          <b-form-invalid-feedback :state="latitude.state">
          {{latitude.error}}
            </b-form-invalid-feedback>
        </b-form-group>

        <b-form-group
          label="Geografska \u0161irina:"
        >
          <b-form-input
            :state="longitude.state"
            v-model="longitude.value"
            type="text"
            placeholder="Unesite geografsku \u0161irinu"
            v-on:blur="validateLongitude"
          ></b-form-input>
          <b-form-invalid-feedback :state="longitude.state">
          {{longitude.error}}
            </b-form-invalid-feedback>
        </b-form-group>

        <b-form-group
          label="Ulica:"
        >
          <b-form-input
            :state="street.state"
            v-model="street.value"
            type="text"
            placeholder="Unesite ulicu"
            v-on:blur="validateStreet"
          ></b-form-input>
          <b-form-invalid-feedback :state="street.state">
          {{street.error}}
            </b-form-invalid-feedback>
        </b-form-group>

        <b-form-group
          label="Broj u ulici:"
        >
          <b-form-input
            :state="number.state"
            v-model="number.value"
            type="text"
            placeholder="Unesite broj u ulici"
            v-on:blur="validateNumber"
          ></b-form-input>
          <b-form-invalid-feedback :state="number.state">
          {{number.error}}
            </b-form-invalid-feedback>
        </b-form-group>

        <b-form-group
          label="Grad:"
        >
          <b-form-input
            :state="city.state"
            v-model="city.value"
            type="text"
            placeholder="Unesite grad"
            v-on:blur="validateCity"
          ></b-form-input>
          <b-form-invalid-feedback :state="city.state">
          {{city.error}}
            </b-form-invalid-feedback>
        </b-form-group>

        <b-form-group
          label="Po\u0161tanski broj:"
        >
          <b-form-input
            v-model="postalCode.value"
            type="text"
            placeholder="Unesite po\u0161tanski broj"
          ></b-form-input>
        </b-form-group>

        <b-form-group
          label="Dr\u017Eava:"
        >
          <b-form-input
            :state="country.state"
            v-model="country.value"
            type="text"
            placeholder="Unesite dr\u017Eavu"
            v-on:blur="validateCountry"
          ></b-form-input>
          <b-form-invalid-feedback :state="country.state">
          {{country.error}}
            </b-form-invalid-feedback>
        </b-form-group>
  
        <b-form-group
          label="Cena no\u0107enja:"
        >
          <b-form-input
            :state="pricePerNight.state"
            v-model="pricePerNight.value"
            type="text"
            placeholder="Unesite cenu no\u0107enja"
            v-on:blur="validatePricePerNight"
          ></b-form-input>
          <b-form-invalid-feedback :state="pricePerNight.state">
          {{pricePerNight.error}}
            </b-form-invalid-feedback>
        </b-form-group>
  
        <b-form-group
          label="Vreme prijave:"
        >
          <b-form-timepicker
            :state="checkInTime.state"
            v-model="checkInTime.value"
            v-on:blur="validateCheckInTime"
          ></b-form-input>
          <b-form-invalid-feedback :state="checkInTime.state">
          {{checkInTime.error}}
            </b-form-invalid-feedback>
        </b-form-group>
  
        <b-form-group
          label="Vreme odjave:"
        >
          <b-form-timepicker
            :state="checkOutTime.state"
            v-model="checkOutTime.value"
            v-on:blur="validateCheckOutTime"
          ></b-form-input>
          <b-form-invalid-feedback :state="checkOutTime.state">
          {{checkOutTime.error}}
            </b-form-invalid-feedback>
        </b-form-group>
  
        <b-form-group
          label="Datumi za izdavanje:"
        >
        <v-date-picker
          v-model="datesForRenting"
          mode="multiple"
          is-inline
          />
          </b-form-group>
  
          <b-form-group label="Sadr\u017Eaji apartmana:">
          <b-form-checkbox-group v-model="amenities" name="amenities" :options="allAmenities">
          </b-form-checkbox-group>
        </b-form-group>
                    
        <b-button class="mt-2 mb-2 float-right" type="submit" variant="primary" :disabled="!formValid">Izmeni apartman</b-button>
      
      </b-form> 
      
    </b-col> 

    <b-col>

    <b-row>
        <b-col class="border rounded mt-2 mb-2 pt-2">
        <b-alert
      v-model="imageAlert.show"
      dismissible>
      {{ imageAlert.text }}
    </b-alert>   
        <h3>Izmena slika apartmana</h3>
            <b-form class="p-1" inline v-on:submit="imageSubmit"> 
                    <b-button class="m-1" type="submit" variant="primary">Dodaj sliku</b-button>
                    <input type="file" class="m-1" v-on:change="change"></input>
            </b-form>

        <b-row class="p-2">
            <b-col v-for="image in images" class="m-2 p-1 border rounded" md="auto">
                <img style="max-width: 200px;" v-bind:src="image.data"/><br>
                <b-button block class="mt-2" variant="danger" @click="deleteImage(image)">
                <b-icon icon="x"></b-icon>
                     Obri\u0161i
                </b-button>
            </b-col>
        </b-row>

        </b-col>
    </b-row>

    <b-row>
        <b-col class="border rounded mb-2 pt-2">
        <b-alert
        v-model="commentAlert.show"
        dismissible>
        {{ commentAlert.text }}
      </b-alert>   
          <h3>Izmena komentara apartmana</h3>

          <dl>
          <dd v-for="comment in comments">
              <b-card>
                  <b-container>
                      <b-row>
                          
                          <b-col cols="5">
                              <i>{{comment.guest.name + ' ' + comment.guest.surname}}</i>
                              <br>
                              {{comment.text}}
                          </b-col>
                          <b-col>
                              <h1 style="font-size:35px;">
                                  <b-badge variant="success">
                                  {{comment.rating}}</b-badge>
                              </h1>
                          </b-col>
                          <b-col cols="3">
                          <b-button v-if="comment.showing" block class="mt-2" variant="danger" @click="hide(comment)">                            
                                Sakrij
                            </b-button>
                            <b-button v-else block class="mt-2" variant="primary" @click="show(comment)">                            
                                Prika\u017Ei
                            </b-button>
                          </b-col>
                      </b-row>
  
                    
                  </b-container>
              </b-card>
          </dd>
      </dl>

        </b-col>
    </b-row>

    </b-col>
    </b-row>
  </b-container>
      `

});