Vue.component("login", {
    data() {
        return {
            username: {
                value: null,
                state: null,
                error: null
            },
            password: {
                value: null,
                state: null,
                error: null
            },
            alert: {
                text: '',
                show: false
            }
        }
    },
    computed: {
        formValid: function () {
            return this.username.state && this.password.state
        }
    },
    methods: {
        submit(event) {
            event.preventDefault();
            this.validateUsername();
            this.validatePassword();

            if (this.username.state && this.password.state) {
                axios.post("/WebApartmani/rest/auth/login",
                    {
                        username: this.username.value,
                        password: this.password.value
                    }).then(response => this.loggedIn()).catch(
                        error => { this.alert.text = "Logovanje nije uspelo: " + error.response.data; this.alert.show = true; }
                    );
            }
        },
        loggedIn(){
        	axios.get("/WebApartmani/rest/profile").then(
    				response => this.redirect(response.data)).catch(
    						error => { this.alert.text = "Logovanje nije uspelo: " + error.response.data; this.alert.show = true;});
        },
        redirect(user){
			if (user.role === "Administrator")
				window.location.href = "http://localhost:8081/WebApartmani/admin.html";            		
			else if (user.role === "Gost")
				window.location.href = "http://localhost:8081/WebApartmani/guest.html";
			else if (user.role === "Doma\u0107in")
				window.location.href = "http://localhost:8081/WebApartmani/host.html";
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
            this.password.state = true;
        }
    },
    template: `
    <b-container class="w-50 p-5">
    <b-row>
    <b-col class="border rounded m-2 pt-2">
  <b-alert
    v-model="alert.show"
    dismissible>
    {{ alert.text }}
  </b-alert>    
  <b-form v-on:submit="submit">
     
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
        <b-form-invalid-feedback :state="password.state">
        {{password.error}}
      	</b-form-invalid-feedback>
      </b-form-group>
                  
      <b-button class="mt-2 mb-2 float-right" type="submit" variant="primary" :disabled="!formValid">Uloguj se</b-button>
      
    
    </b-form> 
    
  </b-col> 
  </b-row>
</b-container>
    `
});