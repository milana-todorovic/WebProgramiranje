Vue.component("guest-reservations",{
    data: function(){
        return{
            name:'Rezervacije',
            rezervacije:[
                {
                    imeApartmana:'Apartmani Ivana',
                    pocetniDatum:'25.5.2020',
                    krajnjiDatum:'28.10.2020',
                    cenaApartmana:'400$',
                    status:'kreirana'
                },
                {
                    imeApartmana:'Apartmani Ana',
                    pocetniDatum:'2.8.2020',
                    krajnjiDatum:'28.8.2020',
                    cenaApartmana:'300$',
                    status:'odbijena'
                },
                {
                    imeApartmana:'Sobe Slobodan Bajic',
                    pocetniDatum:'21.10.2020',
                    krajnjiDatum:'22.10.2020',
                    cenaApartmana:'40$',
                    status:'prihvacena'
                }
            ],
            options3:[
                {text:'Rastu\u0107e',value:'rastuce'},
                {text:'Opadaju\u0107e',value:'opadajuce'}
            ]
           

        }
    },
    template:`
        <div>
            <b-container style="margin-left:1%;">
                <b-row>
                    <b-col sm="2" style="margin-left:1%;margin-top:5%;">
                        <div>
                            <b-card >
                                <b><b-form-group label="Sortiranje po ceni"></b>
                                    <b-form-checkbox-group
                                        :options="options3"
                                        plain
                                        stacked
                                        ></b-form-checkbox-group>
                                    </b-form-group>
                                    <br><br>
                                    <b-button   variant="primary"> 
                                        <b-icon icon="arrow-down-up"></b-icon>
                                        Sortiraj
                                    </b-button>
                            </b-card>
                                
                        </div>
                    </b-col>

                    <b-col>
                        <div>
                            <dl>
                                <dd v-for="rezervacija in rezervacije">
                                    <b-card style="max-width: 840px;margin-top:6%;">
                                        

                                        <b-container>
                                            <b-row>
                                                <b-col>
                                                    <h1 id="nazivApartmana">
                                                        {{rezervacija.imeApartmana}}
                        
                                                    </h1>
                                                    <div style="background-color:teal;padding:5%;color:white;font-size:18px">
                                                        Od  <b>{{rezervacija.pocetniDatum}}</b>
                                                        <br>
                                                        Do  <b>{{rezervacija.krajnjiDatum}}</b>
                                                        <br>
                                                        Po ceni  <b>{{rezervacija.cenaApartmana}}</b>
                                                    </div>
                                                </b-col>
                                                
                                                <b-col>
                                                        <h1 style="font-size:30px;margin-top:9%">
                                                            <b-badge variant="success" >{{rezervacija.status}}</b-badge>
                                                        </h1>
                                                    
                                                    <br>
                                                    <b-button variant="outline-danger" @click="rezervacija.status='odustanak' ">
                                                        Odustani
                                                    </b-button>
                                                </b-col>
                                            </b-row>
                                            <br>
                                            <hr class="solid" style=" border-top: 1px solid #bbb;">

                                            <b-row>
                                                <b-col>
                                                    <br>
                                                    Va&#x161; komentar:
                                                    
                                                    <b-form-textarea placeholder="Unesite komentar"></b-form-textarea>
                                                    <br>
                                                    Va&#x161;a ocena:
                                                
                                                    <b-form-rating stars="10" show-value precision="1"></b-form-rating>
                                                    <br>

                                                    <b-button variant="outline-primary">
                                                        Ostavi komentar
                                                    </b-button>
                                                </b-col>
                                            </b-row>
                                        </b-container>
                                        
                                    

                                        
                                    </b-card>
                                </dd>
                            </dl>
                        </div>
                    </b-col>
                </b-row>
            </b-container>
            
        </div>
    `
});