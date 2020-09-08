Vue.component("guest-profile", {
    data: function () {
        return {
            name: "Korisnikov profil",
            osoba:
            {
                imeKorisnika: "Ana",
                prezimeKorisnika: "Maksimovic",
                password: "anamaksi12",
                pol: "zenski",
                
            },
            passwordDisable: true,
            nameDisable:true,
            surnameDisable:true,
            genderDisable:true


        }
    },
    template: `
        <div>
            <b-container>
                <b-card style="max-width: 740px;margin-top:5%;padding:10%;">
                    
                        <b-row>
                            <b-col>
                                <h1>Vas profil i licni podaci</h1>
                            
                            <br>
                                <label>Ime:</label>
                                <b-form-input :disabled=nameDisable v-model="osoba.imeKorisnika"></b-form-input>
                                <b-buton>Uredi</b-button>
                            <br>
                                <label>Prezime:</label>
                                <b-form-input :disabled=surnameDisable v-model="osoba.prezimeKorisnika"></b-form-input>
                            
                            <br>
                                <label>Pol: </label>
                                <b-form-input :disabled=genderDisable v-model="osoba.pol"></b-form-input>
                            <br>
                          
                               <label>Trenutna lozinka: </label> {{osoba.password}}
                              
                            <br>
                                
                                <label>Nova lozinka: </label>
                                <b-form-input  :disabled=passwordDisable v-model="osoba.password"></b-form-input> 
                            
                            <br>
                                <label>Ponovno ukucavanje nove lozinke:</label>
                                <b-form-input></b-form-input>
                            <br>
                            
                                <b-button variant="primary" @click="promeni"> Uredi </b-button>
                                
                        

                        </b-col>
                    </b-row>
                </b-card>
            </b-container>
           
        </div>
           
    `,
    methods: {
        promeni: function(){
            passwordDisable = !passwordDisable
        }
    },

});