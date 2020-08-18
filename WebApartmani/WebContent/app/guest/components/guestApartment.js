Vue.component("guest-apartments",{
    data: function(){
        return{
            name: 'Apartmani',
            show: true,
            dismissSecs: 2,
            dismissCountDown: 0,
            apartmani:[
                {
                    mico:'Apartmani Ivana',
                sadrzaj:'Jastuk,krevet,jastucnica,posudje',
                brojSoba:5,
                aktivan:false
                },
                {
                mico:'Apartmani Mico',
                sadrzaj:'Jastuk,krevet,jastucnica,posudje',
                brojSoba:6,
                aktivan:true
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
    methods: {
        countDownChanged(dismissCountDown) {
            this.dismissCountDown = dismissCountDown
          },
        showAlert() {
            this.dismissCountDown = this.dismissSecs
          }
      },
    template:`
    <div>
        <div id="pretraga">
            <b-form inline>
                <b-form-input  placeholder="Lokacija"></b-form-input>
                <b-form-input   placeholder="Broj osoba"></b-form-input>
                <b-form-input   placeholder="Broj soba"></b-form-input>
                <b-form-input   placeholder="Cena"></b-form-input>
                <b-form-datepicker  placeholder="Pocetni datum"></b-form-datepicker>
                <b-form-datepicker  placeholder="Krajnji datum"></b-form-datepicker>
                
                <b-button pill id="pretrazi" variant="primary">
                    <b-icon icon="search"></b-icon>
                    Pretrazi
                </b-button>
            </b-form>
        </div>
    
        <br><br>
        <b-containter>
            <b-col>
                <div id="filtriranje">
                    <b><b-form-group label="Sortiranje po ceni"></b>
                        <b-form-checkbox-group
                        :options="options3"
                        plain
                        stacked
                        ></b-form-checkbox-group>
                    </b-form-group>
                    <br><br>
                    <b-button id="filtrirajDugme" pill variant="primary"> 
                        <b-icon icon="arrow-down-up"></b-icon>
                        Sortiraj
                    </b-button>
                </div>
                <br>
           
                <div id="filtriranje">
                   
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

                    <b-button id="filtrirajDugme"pill variant="primary">
                        <b-icon icon="funnel-fill"></b-icon>
                            Filtriraj
                    </b-button>
                            
                </div>
                
                    
            </b-col>


           
                
              
          
        </b-container>

    </div>

    `
});