Vue.component("host-addApartment", {
  data: function () {
    return {
      name: { value: null, state: null, error: null },
      numberOfRooms: { value: null, state: null, error: null },
      numberOfGuests: { value: null, state: null, error: null },
      pricePerNight: { value: null, state: null, error: null },
      checkInTime: { value: "14:00:00", state: true, error: null },
      checkOutTime: { value: "10:00:00", state: true, error: null },
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
        && this.pricePerNight.state && this.checkInTime.state && this.checkOutTime.state;
    }
  },
  methods: {
    fetchAmenities() {
      axios.get("/WebApartmani/rest/amenities").then(
        response => this.amenitiesRead(response.data)).catch(
          error => this.setGlobalAlert("Došlo je do greške pri učitavanju sadržaja apartmana."));
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
          host: { id: 2 },
          name: this.name.value,
          apartmentType: this.type,
          numberOfRooms: this.numberOfRooms.value,
          numberOfGuests: this.numberOfGuests.value,
          pricePerNight: this.pricePerNight.value,
          checkInTime: this.checkInTime.value,
          checkOutTime: this.checkOutTime.value,
          datesForRenting: dates,
          amenities: this.amenities,
          location: { latitude: 19, longitude: 45 }
        }
      ).then(response => this.setGlobalAlert("Dodavanje je uspešno izvršeno.")).catch(
        error => this.setGlobalAlert("Dodavanje nije uspelo: " + error.response.data));
    },
    reset() {
      this.name = { value: null, state: null, error: null };
      this.numberOfRooms = { value: null, state: null, error: null };
      this.numberOfGuests = { value: null, state: null, error: null };
      this.pricePerNight = { value: null, state: null, error: null };
      this.checkInTime = { value: null, state: null, error: null };
      this.checkOutTime = { value: null, state: null, error: null };
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
        this.numberOfRooms.error = "Broj soba mora biti pozitivan broj veći od 1."
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
        this.numberOfGuests.error = "Broj gostiju mora biti pozitivan broj veći od 1."
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
        label="Cena noćenja:"
      >
        <b-form-input
          :state="pricePerNight.state"
          v-model="pricePerNight.value"
          type="text"
          placeholder="Unesite cenu noćenja"
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

        <b-form-group label="Sadržaji apartmana:">
        <b-form-checkbox-group v-model="amenities" name="amenities" :options="allAmenities">
        </b-form-checkbox-group>
      </b-form-group>
                  
      <b-button class="mt-2 mb-2 float-right" type="submit" variant="primary" :disabled="!formValid">Registruj</b-button>
      <b-button class="mr-2 mt-2 mb-2 float-right" type="reset" variant="secondary">Resetuj formu</b-button>
    
    </b-form> 
    
  </b-col> 
  </b-row>
</b-container>
    `

});