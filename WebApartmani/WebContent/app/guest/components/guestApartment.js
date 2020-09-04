Vue.component("guest-apartments",{
    data: function(){
        return{
            name: 'Apartmani',
            apartmani:[
                {
                ime:'Apartmani Ivana',
                sadrzaj:'Jastuk,krevet,jastucnica,posudje',
                brojSoba:5,
                aktivan:false,
                tip:'soba',
                brojOsoba:2,
                lokacija:'Pariz',
                cena:'20$',
                prijava:'14:00h',
                odjava:'11:00h',
                komentari:'Apartman za svaku preporuku.Doci cemo ponovo!'
                },
                {
                ime:'Apartmani Mico',
                sadrzaj:'Jastuk,krevet,jastucnica,posudje',
                brojSoba:6,
                aktivan:true,
                tip:'ceo apartman',
                brojOsoba:4,
                lokacija:'Santorini',
                cena:'40$',
                prijava:'14:00h',
                odjava:'12:00h',
                komentari:'Udaljeno od centra,ali veoma cisto.'
                }
            ],
            options: [
                { text: 'Ceo', value: 'ceo' },
                { text: 'Soba', value: 'soba' },
               
              ],
            options2:[
                {text:'Tus kabina',value:'tus'},
                {text:'Posudje',value:'posudje'},
                {text:'Fen za kosu',value:'fen'},
                {text: 'Pegla',value:'pegla'},
                {text:'Ves masina',value:'ves'},
                {text:'Parking',value:'parking'},
                {text:'Bazen',value:'bazen'},
                {text:'Klima',value:'klima'},
                {text:'Wifi',value:'wifi'},
                {text:'Smart tv',value:'tv'},
                {text:'Grejanje',value:'grejanje'},
                {text:'Peskiri',value:'peskiri'},
                {text:'Frizider',value:'frizider'},
                {text:'Lift',value:'lift'}
            ],
            options3:[
                {text:'Rastuce',value:'rastuce'},
                {text:'Opadajuce',value:'opadajuce'}
            ]
        }
    
    },
    template:`
    <div>
           
            <b-card id="pretraga">
                <b-card-text>
                    <b-form inline>
                        <b-form-input  placeholder="Lokacija"></b-form-input>
                        <b-form-input   placeholder="Broj osoba"></b-form-input>
                        <b-form-input   placeholder="Broj soba"></b-form-input>
                        <b-form-input   placeholder="Cena po no&#x107;enju"></b-form-input>
                        <b-form-datepicker  placeholder="Po&#269;etni datum"></b-form-datepicker>
                        <b-form-datepicker  placeholder="Krajnji datum"></b-form-datepicker>
                        
                        <b-button   variant="primary">
                            <b-icon icon="search"></b-icon>
                            Pretrazi
                        </b-button>
                    </b-form>
                </b-card-text>
            </b-card>
            
    
    
        <br><br>
        <b-containter>
            <b-row align-v="start">
                <b-col sm="2">
                    <div>
                        <b-card id="filtriranje">
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
                        <b-card id="filtriranje">
                            <b-card-text>
                                <b><b-form-group label="Tip apartmana"></b>
                                <b-form-checkbox-group
                                    :options="options"
                                    plain
                                    stacked
                                    ></b-form-checkbox-group>
                                </b-form-group>

                                <br><br>

                                <b><b-form-group label="Sadrzaji"></b>
                                    <b-form-checkbox-group
                                        :options="options2"
                                        plain
                                        stacked
                                        ></b-form-checkbox-group>
                                    </b-form-group>
                            
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
                            <dd v-for="apartman in apartmani">
                
                                <b-card style="max-width: 740px;margin-left:10%" no-body>
                    
                                    <b-row no-gutters>
                                        <b-col>
                                            <b-card-img src="https://picsum.photos/400/400/?image=20" 			alt="Image" class="rounded-0"></b-card-img>
                                        </b-col>
                                        
                                        <b-col>
                                            <b-card-body>
                                                <b-card-text>
                                                    <h1 id="nazivApartmana">
                                                    {{apartman.ime}} <b-badge variant="success">{{apartman.tip}}</b-badge>
                                                    </h1>

                                                    

                                                    <br>
                                                    <b-icon icon="geo-alt" style="width: 25px; height: 25px;">
                                                    </b-icon>

                                                    <span style="font-size:20px">
                                                        {{apartman.lokacija}}
                                                    </span>
                                                  
                                                    <br>
                                                    <span style="font-size:25px">
                                                      <b>{{apartman.cena}}</b>
                                                    </span>
                                                    
                                                        <i>po no&#x107;enju</i>
                                                   
                                                    <br><br><br><br><br>
                                                    <b-button variant="primary">
                                                        Prika&#x17E;i detalje
                                                        <b-icon icon="arrow-right">
                                                    </b-button>
                                                   
                        
                                    
                                                </b-card-text>
                                            </b-card-body>
                                        </b-col>
                                    </b-row>
                                </b-card>
                                <br><br>
                        
                        
                            </dd>
                        </dl>
                    </div>
                </b-col>
            </b-row>


           
                
              
          
        </b-container>

    </div>

    `
});