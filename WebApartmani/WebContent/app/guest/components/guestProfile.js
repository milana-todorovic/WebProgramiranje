Vue.component("guest-profile", {
    data() {
        return {
            user: null,
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
            oldPassword: {
                value: null,
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
            },
            palert: {
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
            return this.username.state && this.gender.state && this.name.state && this.surname.state
        }
    },
    methods: {
        subPassword(event) {
            event.preventDefault();
            this.validatePassword();
            this.validateOldPassword();
            if (this.password.state && this.oldPassword.state) {
                axios.put("/WebApartmani/rest/profile/password",
                    {
                        oldPassword: this.oldPassword.value,
                        newPassword: this.password.value
                    }).then(response => this.passChanged()).catch(
                        error => { this.palert.text = "Izmena lozinke nije uspela: " + error.response.data; this.palert.show = true; }
                    );
            }
        },
        passChanged() {
            this.password.value = null;
            this.password.repeatedValue = null;
            this.password.state = null;
            this.password.error = null;
            this.oldPassword.value = null;
            this.oldPassword.state = null;
            this.oldPassword.error = null;
            this.palert.text = "Lozinka je promenjena.";
            this.palert.show = true;
        },
        submit(event) {
            event.preventDefault();
            this.validateName();
            this.validateSurname();
            this.validateUsername();
            this.validateGender();

            if (this.username.state && this.gender.state && this.name.state && this.surname.state) {
                axios.put("/WebApartmani/rest/profile/personal-info",
                    {
                        username: this.username.value,
                        name: this.name.value,
                        surname: this.surname.value,
                        gender: this.getGender()
                    }).then(response => this.updated()).catch(
                        error => { this.alert.text = "Izmena podataka nije uspela: " + error.response.data; this.alert.show = true; }
                    );
            }
        },
        updated() {
            this.user.username = this.username.value;
            this.user.name = this.name.value;
            this.user.surname = this.surname.value;
            this.user.gender = this.getGender();
            this.alert.text = "Podaci su izmenjeni.";
            this.alert.show = true;
        },
        reset() {
            this.username.value = this.user.username;
            this.username.state = true;
            this.username.error = null;
            this.name.value = this.user.name;
            this.name.state = true;
            this.name.error = null;
            this.surname.value = this.user.surname;
            this.surname.state = true;
            this.surname.error = null;
            if (this.user.gender === "mu\u0161ki" || this.user.gender === "\u017Eenski" || this.user.gender === "nepoznat") {
                this.gender.selected = this.user.gender;
                this.gender.typed = null;
                this.gender.state = true;
                this.gender.error = null;
            } else {
                this.gender.selected = "drugi";
                this.gender.typed = this.user.gender;
                this.gender.state = true;
                this.gender.error = null;
            }
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
        validateOldPassword() {
            if (!this.oldPassword.value) {
                this.oldPassword.error = "Stara lozinka je obavezna."
                this.oldPassword.state = false;
                return;
            }
            this.oldPassword.state = true;
        },
        validatePassword() {
            if (!this.password.value) {
                this.password.error = "Nova lozinka je obavezna."
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
        },
        delete() {
            axios.delete("/WebApartmani/rest/profile").then(
                axios.post("/WebApartmani/rest/auth/logout").then(
                    response =>
                        window.location.href = "http://localhost:8081/WebApartmani/"
                )
            ).catch(
                error => { this.alert.text = "Brisanje nije uspelo: " + error.response.data; this.alert.show = true; }
            );
        }
    },
    created() {
        axios.get("/WebApartmani/rest/profile").then(
            response => { this.user = response.data; this.reset(); }
        ).catch(
            error => { this.alert.text = "Dobavljanje podataka nije uspelo: " + error.response.data; this.alert.show = true; }
        )
    },
    template: `
    <b-container class="p-5" fluid>
      <b-row align-v="start">


      <b-col class="border rounded m-2 pt-2">

  <b-alert
      v-model="alert.show"
      dismissible>
      {{ alert.text }}
    </b-alert> 

  <b-form v-on:submit="submit">

        <h3>Izmena li\u010Dnih podataka</h3>
      <b-form-group
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
                  
      <b-button class="mt-2 mb-2 float-right" type="submit" variant="primary" :disabled="!formValid">Izmeni podatke</b-button>
    
    </b-form> 
 </b-col>  

 
 <b-col class="border rounded m-2 pt-2">
 <b-alert
 v-model="palert.show"
 dismissible>
 {{ palert.text }}
</b-alert> 

    <b-form v-on:submit="subPassword">

    <h3>Izmena lozinke</h3>

    <b-form-group
        label="Stara lozinka:"
        >
        <b-form-input
          :state="oldPassword.state"
          v-model="oldPassword.value"
          type="password"
          placeholder="Unesite lozinku"
          v-on:blur="validateOldPassword"
        ></b-form-input>
      	<b-form-invalid-feedback :state="oldPassword.state">
        {{oldPassword.error}}
      	</b-form-invalid-feedback>
      </b-form-group>

    <b-form-group
        label="Nova lozinka:"
        >
        <b-form-input
          :state="password.state"
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

      <b-button class="mt-2 mb-2 float-right" type="submit" variant="primary" :disabled="!(this.password.state && this.oldPassword.state)">Izmeni lozinku</b-button>
      
    
    </b-form>
    </b-col>  

  </b-row>
</b-container>
    `
});