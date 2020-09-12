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
                uloga:"gost"
                
            }


        }
    },
    template: `
        
        <div>
            <b-container>
                <b-row>
                    <b-col>
                        <b-card style="max-width:680px;margin-top:5%;">
                            <h1>Korisnikovi podaci</h1>

                            <br>
                            <b><label>Ime korisnika</label></b>
                            <b-form-input></b-form-input>
                            <br>

                            <b><label>Prezime korisnika</label></b>
                                <b-form-input></b-form-input>
                            <br>

                            <b><label>Uloga<label></b>
                                <b-form-input></b-form-input>
                            <br>

                            <b><label>Pol</label></b>
                                <b-form-input></b-form-input>
                            <br>
                            
                          
                                     
                            <b><label>Nova lozinka</label></b>
                                <b-form-input></b-form-input>
                            <br>
                            <b><label>Ponovno ukucavanje nove lozinke</label></b>
                                <b-form-input></b-form-input>
                            <br>
                                            
                            

                            <b-button variant="primary">
                                <b-icon icon="pencil-square"></b-icon>
                                Izmeni
                            </b-button>
                        </b-card>

                        

                    <br>
                    </b-col>
                </b-row>
            </b-container>
     
        </div>
           
      
           
    `
});