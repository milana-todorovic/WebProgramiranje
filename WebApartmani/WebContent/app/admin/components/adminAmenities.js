Vue.component("admin-amenities", {
    data: function () {
        return {
            amenities: [],
            mock: { id: null, name: null, state: null, error: null },
            globalAlert: { show: false, text: null },
            updateMode: false
        }

    },
    created() {
    	this.fetchAmenities();
    },
    watch: {
        '$route': 'fetchAmenities'
    },
    methods: {
        fetchAmenities() {
            axios.get("/WebApartmani/rest/amenities").then(
                response => this.fetched(response.data)).catch(
                    error => setGlobalAlert("Došlo je do greške pri učitavanju sadržaja apartmana."));
        },
        addAmenity() {
            this.validateMock();
            if (this.mock.state)
                axios.post("/WebApartmani/rest/amenities", { name: this.mock.name }).then(
                    response => this.added(response.data)).catch(
                        error => setGlobalAlert("Dodavanje nije uspelo: " + error.response.data));
        },
        updateAmenity() {
            this.validateMock();
            if (this.mock.state)
                axios.put("/WebApartmani/rest/amenities/" + this.mock.id, { name: this.mock.name, id: this.mock.id }).then(
                    response => this.updated(response.data)).catch(
                        error => setGlobalAlert("Izmena nije uspela: " + error.response.data));
        },
        deleteAmenity(amenity) {
            axios.delete("/WebApartmani/rest/amenities/" + amenity.id).then(
                response => this.deleted(amenity)).catch(
                    error => { amenity.text = "Brisanje nije uspelo: " + error.response.data; amenity.show = true; });
        },
        startUpdate(amenity) {
            this.mock.id = amenity.id;
            this.mock.name = amenity.name;
            this.updateMode = true;
            this.validateMock();
        },
        cancelUpdate() {
            this.updateMode = false;
            this.mock = { id: null, name: null, state: null, error: null };
        },
        fetched(newAmenities) {
            this.updateMode = false;
            this.mock = { id: null, name: null, state: null, error: null };
            this.globalAlert = { show: false, text: null };
            this.amenities = [];
            for (amenity of newAmenities)
                this.amenities.push({ name: amenity.name, id: amenity.id, deleted: false, show: false, text: null });
        },
        added(amenity) {
            this.mock = { id: null, name: null, state: null, error: null };
            this.amenities.push({ name: amenity.name, id: amenity.id, deleted: false, show: false, text: null });
            this.setGlobalAlert("Dodavanje je uspešno izvršeno.")
        },
        updated(amenity) {
            this.updateMode = false;
            this.mock = { id: null, name: null, state: null, error: null };
            for (current of this.amenities)
                if (current.id === amenity.id) {
                    current.name = amenity.name;
                    current.show = false;
                    current.text = null;
                }
            this.setGlobalAlert("Izmena je uspešno izvršena.")
        },
        deleted(amenity) {
            amenity.deleted = true;
            amenity.text = "Sadržaj apartmana je uspešno obrisan."
            amenity.show = true;
        },
        setGlobalAlert(text) {
            this.globalAlert.text = text;
            this.globalAlert.show = true;
        },
        validateMock() {
            if (!this.mock.name) {
                this.mock.error = "Naziv sadržaja apartmana je obavezan."
                this.mock.state = false;
                return;
            }
            this.mock.state = true;
        }
    },
    template: `
    <b-container class="pt-2 pb-2 w-75">
        <b-row class="border rounded p-2">
            <b-col>
                <b-alert
                v-model="globalAlert.show"
                dismissible>
                {{ globalAlert.text }}
                </b-alert>

                <b-form inline>
                <b-form-group
                id="input-group-1"
                label="Naziv:"
                label-for="name"
              >
                <b-form-input
                  :state="mock.state"
                  id="name"
                  v-model="mock.name"
                  type="text"
                  placeholder="Unesite naziv sadržaja apartmana"
                  v-on:change="validateMock"
                ></b-form-input>
                <b-form-invalid-feedback :state="mock.state">
                {{mock.error}}
                  </b-form-invalid-feedback>
              </b-form-group>

              <b-button v-on:click="addAmenity" v-if="!updateMode" variant="primary" :disabled="!mock.state">Dodaj</b-button>
              <b-button v-on:click="updateAmenity" v-if="updateMode" variant="primary" :disabled="!mock.state">Izmeni</b-button>
              <b-button v-on:click="cancelUpdate" v-if="updateMode" variant="secondary" :disabled="!mock.state">Odustani</b-button>
                </b-form inline>
            </b-col>
        </b-row>
        <b-row class="pt-2">
            <b-col>
                <b-row v-for="amenity in amenities" class="w-100">
                <b-col class=" border rounded p-2 m-2" v-if="!amenity.deleted || amenity.show">
                <b-alert
                v-model="amenity.show"
                dismissible>
                    {{ amenity.text }}
                </b-alert>

                <b-row v-if="!amenity.deleted">
                    <b-col class="w-75">
                        <p class="h3">{{amenity.name}}</p>
                    </b-col>
                    <b-col  class="w-25">
                        <b-button v-if="!updateMode" v-on:click="deleteAmenity(amenity)" variant="danger" class="w-100">Obriši</b-button>
                        <b-button v-if="!updateMode" v-on:click="startUpdate(amenity)" variant="success" class="w-100 mt-2">Izmeni</b-button>
                    </b-col>
                    </b-col>
                </b-row>
                <b-row>
            </b-col>
        </b-row>
    </b-container fluid>
    `
});