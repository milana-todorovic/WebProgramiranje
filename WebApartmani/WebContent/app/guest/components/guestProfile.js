Vue.component("guest-profile",{
    data: function(){
        return{
            name:"Korisnikov profil",
            osoba:[
                {
                    imeKorisnika:"Ana",
                    prezimeKorisnika:"Maksimovic",
                    username:"anamaksi12",
                    pol:"zenski",
                    uloga:"gost"
                }
            ]
        }
    },
    template:`
        <div>
           <b-card>
                <b-row>
                    <b-col>
                        
                        
                      
                    </b-col>
                </b-row>
           </b-card>
        </div>
           
    `

});