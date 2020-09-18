Vue.component("host-addApartment", {
  data: function () {
    return {
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
      postalCode: { value: null, state: true, error: null },
      country: { value: null, state: null, error: null },
      type: "Soba",
      datesForRenting: [],
      amenities: [],
      allAmenities: [],
      globalAlert: { show: false, text: null },
      zoom: 13
    }
  },
  created() {
    this.fetchAmenities();
  },
  watch: {
    '$route': 'fetchAmenities'
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
      if (!this.formValid)
        return;
      let dates = [];
      for (date of this.datesForRenting)
        dates.push(date.getTime() - date.getTimezoneOffset() * 60 * 1000);
      axios.post("/WebApartmani/rest/apartments",
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
          location: {
            latitude: this.latitude.value,
            longitude: this.longitude.value,
            address: {
              street: this.street.value,
              number: this.number.value,
              city: this.city.value,
              postalCode: this.postalCode.value,
              country: this.country.value
            }
          }
        }
      ).then(response => { this.setGlobalAlert("Dodavanje je uspe\u0161no izvr\u0161eno."); this.reset(); }).catch(
        error => this.setGlobalAlert("Dodavanje nije uspelo: " + error.response.data));
    },
    reset() {
      this.name = { value: null, state: null, error: null };
      this.numberOfRooms = { value: null, state: null, error: null };
      this.numberOfGuests = { value: null, state: null, error: null };
      this.pricePerNight = { value: null, state: null, error: null };
      this.checkInTime = { value: null, state: null, error: null };
      this.checkOutTime = { value: null, state: null, error: null };
      this.longitude = { value: null, state: null, error: null };
      this.latitude = { value: null, state: null, error: null };
      this.street = { value: null, state: null, error: null };
      this.number = { value: null, state: null, error: null };
      this.city = { value: null, state: null, error: null };
      this.country = { value: null, state: null, error: null };
      this.postalCode = { value: null, state: true, error: null };
      this.type = "Soba";
      this.datesForRenting = [];
      this.amenities = [];
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
    }
  },
  template: `
    <b-container class="w-50">
    <b-row>
    <b-col class="border rounded m-2 pt-2">
  <b-alert
    v-model="globalAlert.show"
    dismissible>
    {{ globalAlert.text }}
  </b-alert>   

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
                  
      <b-button class="mt-2 mb-2 float-right" type="submit" variant="primary" :disabled="!formValid">Dodaj apartman</b-button>
      <b-button class="mr-2 mt-2 mb-2 float-right" type="reset" variant="secondary">Resetuj formu</b-button>
    
    </b-form> 
    
  </b-col> 
  </b-row>
</b-container>
    `

});