Vue.component("create-host", {
    data() {
        return {
            username: {
                value: null,
                state: null,
                error: null
            },
            password: {
                value: null,
                repeatedValue: null,
                state: null,
                error: null
            },
            name: {
                value: null,
                state: null,
                error: null
            },
            surname: {
                value: null,
                state: null,
                error: null
            },
            gender: {
                selected: "nepoznat",
                typed: null,
                state: true,
                error: null
            },
            alert: {
                text: '',
                show: false
            }
        }
    },
    computed: {
        genderTypingOff: function () {
            return !(this.gender.selected === "drugi");
        },
        genderState: function () {
            if (this.genderTypingOff)
                return null;
            else
                return this.gender.state;
        },
        formValid: function () {
            return this.username.state && this.password.state && this.gender.state && this.name.state && this.surname.state
        }
    },
    methods: {
        submit(event) {
            event.preventDefault();
            this.validateName();
            this.validateSurname();
            this.validateUsername();
            this.validatePassword();
            this.validateGender();

            if (this.username.state && this.password.state && this.gender.state && this.name.state && this.surname.state) {
                axios.post("/WebApartmani/rest/users",
                    {
                        username: this.username.value,
                        password: this.password.value,
                        name: this.name.value,
                        surname: this.surname.value,
                        gender: this.getGender(),
                        role: "Doma\u0107in"
                    }).then(response => { this.alert.text = "Uspe\u0161no je izvr\u0161eno dodavanje."; this.alert.show = true; this.reset(); }).catch(
                        error => { this.alert.text = "Dodavanje nije uspelo: " + error.response.data; this.alert.show = true; }
                    );
            }
        },
        reset() {
            this.username.value = null;
            this.username.state = null;
            this.username.error = null;
            this.name.value = null;
            this.name.state = null;
            this.name.error = null;
            this.surname.value = null;
            this.surname.state = null;
            this.surname.error = null;
            this.password.value = null;
            this.password.repeatedValue = null;
            this.password.state = null;
            this.password.error = null;
            this.gender.selected = "nepoznat";
            this.gender.typed = null;
            this.gender.state = true;
            this.gender.error = null;
        },
        validateName() {
            if (!this.name.value) {
                this.name.error = "Ime je obavezno."
                this.name.state = false;
                return;
            }
            this.name.state = true;
        },
        validateSurname() {
            if (!this.surname.value) {
                this.surname.error = "Prezime je obavezno."
                this.surname.state = false;
                return;
            }
            this.surname.state = true;
        },
        validateUsername() {
            if (!this.username.value) {
                this.username.error = "Korisni\u010Dko ime je obavezno."
                this.username.state = false;
                return;
            }
            this.username.state = true;
        },
        validatePassword() {
            if (!this.password.value) {
                this.password.error = "Lozinka je obavezna."
                this.password.state = false;
                return;
            }
            if (!(this.password.value == this.password.repeatedValue)) {
                this.password.error = "Lozinke se moraju poklapati."
                this.password.state = false;
                return;
            }
            this.password.state = true;
        },
        validateGender(value) {
        	console.log(value);
            if (value === "drugi" && !this.gender.typed) {            		
                    this.gender.error = "Pol je obavezan."
                    this.gender.state = false;
                    return;
                }
            this.gender.state = true;
        },
        getGender() {
            if (this.gender.selected === "drugi")
                return this.gender.typed;
            else
                return this.gender.selected;
        }
    },
    template: `
    <b-container class="w-50">
    <b-row>
    <b-col class="border rounded m-2 pt-2">
  <b-alert
    v-model="alert.show"
    dismissible>
    {{ alert.text }}
  </b-alert>    
  <b-form v-on:submit="submit" v-on:reset="reset">
      <b-form-group
        id="input-group-1"
        label="Ime:"
        label-for="name"
      >
        <b-form-input
          :state="name.state"
          id="name"
          v-model="name.value"
          type="text"
          placeholder="Unesite ime"
          v-on:blur="validateName"
        ></b-form-input>
        <b-form-invalid-feedback :state="name.state">
        {{name.error}}
      	</b-form-invalid-feedback>
      </b-form-group>
    	<b-form-group
        id="input-group-2"
        label="Prezime:"
        label-for="surname"
        >
        <b-form-input
          :state="surname.state"
          id="surname"
          v-model="surname.value"
          type="text"
          placeholder="Unesite prezime"
          v-on:blur="validateSurname"
        ></b-form-input>
        <b-form-invalid-feedback :state="surname.state">
        {{surname.error}}
      	</b-form-invalid-feedback>
      </b-form-group>
    <b-form-group
        id="input-group-3"
        label="Korisni\u010Dko ime:"
        label-for="username"
        >
        <b-form-input
          :state="username.state"
          id="username"
          v-model="username.value"
          type="text"
          placeholder="Unesite korisni\u010Dko ime"
          v-on:blur="validateUsername"
        ></b-form-input>
      	<b-form-invalid-feedback :state="username.state">
        {{username.error}}
      	</b-form-invalid-feedback>
      </b-form-group>
    <b-form-group
        id="input-group-1"
        label="Lozinka:"
        >
        <b-form-input
          :state="password.state"
          id="password"
          v-model="password.value"
          type="password"
          placeholder="Unesite lozinku"
          v-on:blur="validatePassword"
        ></b-form-input>
      <b-form-input 
          :state="password.state"
          class="mt-2"
          v-model="password.repeatedValue"
          type="password"
          placeholder="Ponovo unesite lozinku"
          v-on:blur="validatePassword"
        ></b-form-input>
      	<b-form-invalid-feedback :state="password.state">
        {{password.error}}
      	</b-form-invalid-feedback>
      </b-form-group>
    
    <b-form-group label="Pol">
      <b-form-radio v-on:change="validateGender('nepoznat')" v-model="gender.selected" name="gender" value="nepoznat">Preferiram da se ne izjasnim</b-form-radio>
      <b-form-radio v-on:change="validateGender('mu\u0161ki')" class="mt-1" v-model="gender.selected" name="gender" value="mu\u0161ki">Mu\u0161ki</b-form-radio>
      <b-form-radio v-on:change="validateGender('\u017Eenski')" class="mt-1" v-model="gender.selected" name="gender" value="\u017Eenski">\u017Denski</b-form-radio>
      <b-form-radio v-on:change="validateGender('drugi')" class="mt-1" v-model="gender.selected" name="gender" value="drugi">Slobodan unos</b-form-radio>
      <b-form-input 
          :state="genderState"
          class="mt-1"
          v-model="gender.typed"
          type="text"
          placeholder="Unesite pol"          
          :disabled="genderTypingOff"
          v-on:blur="validateGender('drugi')"
        ></b-form-input>
      <b-form-invalid-feedback :state="genderState">
        {{gender.error}}
      	</b-form-invalid-feedback>
      </b-form-group>
                  
      <b-button class="mt-2 mb-2 float-right" type="submit" variant="primary" :disabled="!formValid">Registruj</b-button>
      <b-button class="mr-2 mt-2 mb-2 float-right" type="reset" variant="secondary">Resetuj formu</b-button>
    
    </b-form> 
    
  </b-col> 
  </b-row>
</b-container>
    `
});