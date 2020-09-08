Vue.component("guest-host",{
    data: function(){
        return{
            rezervacije:[
                {
                    imeApartmana:'Apartmani Ivana',
                    pocetniDatum:'25.5.2020',
                    krajnjiDatum:'28.10.2020',
                    cenaApartmana:'400$',
                    status:'kreirana',
                    gost:
                    {
                        imeGosta:'Pera',
                        prezimeGosta:'Peric',
                        korisnickoIme:'perica12@',
                        pol:'muski'
                    },
                    poruka:'Parking obezbedjen.aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'
                
                },
                {
                    imeApartmana:'Apartmani Ana',
                    pocetniDatum:'2.8.2020',
                    krajnjiDatum:'28.8.2020',
                    cenaApartmana:'300$',
                    status:'odbijena',
                    gost:
                    {
                        imeGosta:'Pera',
                        prezimeGosta:'Peric',
                        korisnickoIme:'perica12@',
                        pol:'muski'
                    },
                    poruka:'Hocu da mi voda bude ugrijana. Ne zelim buku i zelim fen za kosu.Hvala na razumijevanju.'
                
                },
                {
                    imeApartmana:'Sobe Slobodan Bajic',
                    pocetniDatum:'21.10.2020',
                    krajnjiDatum:'22.10.2020',
                    cenaApartmana:'40$',
                    status:'prihvacena',
                    gost:
                    {
                        imeGosta:'Pera',
                        prezimeGosta:'Peric',
                        korisnickoIme:'perica12@',
                        pol:'muski'
                    },
                    poruka:'Zelim da apartman bude rashladjen.Hvala unapred.'
                
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
                    <b-col>
                        <b-card style=" padding: 0.5%;margin-left:1%;margin-top:1%;margin-right:6%;">
                            <b-card-text>
                                <b-form inline>
                                    <b-form-input   placeholder="Korisni\u010Dko ime gosta"></b-form-input>
                                    
                                    
                                    <b-button   variant="primary" style="margin-left:2%;">
                                        <b-icon icon="search"></b-icon>
                                        Pretra&#x17E;i
                                    </b-button>
                                </b-form>
                            </b-card-text>
                        </b-card>
                    </b-col>
                </b-row>
                <br><br>
                
                <b-row>
                    <b-col sm="2" style="margin-left:1%;">
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
                    
                        <br>
            
                        <div>
                            <b-card>
                                <b-card-text>
                                    <b>Filtriranje po statusu rezervacije</b>
                                    <br><br>
                                        <b-button  variant="primary">
                                            <b-icon icon="funnel-fill"></b-icon>
                                                Filtriraj
                                        </b-button>
                                        
                                 </b-card-text>
                            </b-card>
                        
                            
                        </div>
                    </b-col>
                        
                    

                    <b-col>
                        <div>
                            <dl>
                                <dd v-for="rezervacija in rezervacije">
                                    <b-card style="max-width: 840px;">
                                        

                                        <b-container>
                                            <b-row>
                                                <b-col>
                                                    <h1 id="nazivApartmana">
                                                        {{rezervacija.imeApartmana}}
                        
                                                    </h1>
                                                
                                                
                                                    <div style="background-color:teal;padding:5%;color:white;font-size:18px">
                                                        Gost <b>{{rezervacija.gost.imeGosta}} {{rezervacija.gost.prezimeGosta}}</b>
                                                        <br>
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
                                                    
                                                    <b-button variant="outline-success">
                                                        Prihvati
                                                    </b-button>
                                                
                                                    <b-button variant="outline-secondary">
                                                        Zavr\u0161i
                                                    </b-button>
                                                </b-col>
                                            </b-row>
                                            <br>
                                            <hr class="solid" style=" border-top: 1px solid #bbb;">

                                            <b-row>
                                                <b-col>
                                                
                                                <b>Poruka koju je ostavio gost</b>
                                                <br>
                                                <div style="font-size:18px;">
                                                        {{rezervacija.poruka}}
                                                </div>

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