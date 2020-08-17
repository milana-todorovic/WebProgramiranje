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
            <b-row class="text-center">
                <b-col cols="8">
                    <b-button pill variant="primary"> 
                        <b-icon icon="arrow-down-up"></b-icon>
                        Sortiraj po ceni
                    </b-button>
                
                
                    <b-button pill variant="primary">
                        <b-icon icon="funnel-fill"></b-icon>
                        Filtriraj
                    </b-button>
                <b-col>
            </b-row>
            <br><br><br>
            <b-alert
                :show="dismissCountDown"
                dismissible
                fade
                variant="success"
                @dismiss-count-down="countDownChanged"
            >
            Uspesno ste zakazali apartman!
            </b-alert>
            <b-row class="text-center">
                <b-col>
                    Apartmani
                </b-col>

                <b-col cols="8">
                    <dl>
                        <dd v-for="apartman in apartmani">

                            <b-card no-body class="overflow-hidden" style="max-width: 740px;">
                                <b-row no-gutters>
                                    <b-col md="6">
                                        <b-card-img src="https://picsum.photos/400/400/?image=20" 			alt="Image" class="rounded-0"></b-card-img>
                                    </b-col>

                                    <b-col md="6">
                                        <b-card-body>
                                            <b-card-text>
                                                <b>{{apartman.mico}}</b>
                                                <br><br>
                                    
                                                Sadrzaj: {{ apartman.sadrzaj }}
                                                <br><br>
                                                Broj soba: {{apartman.brojSoba}}
                                            </b-card-text>
                                            <div>
                                        
                                                <b-button @click="showAlert" variant="success">
                                                    Rezervisi apartman
                                                </b-button>
                                                <br><br>
                                            
                                            
                                            </div>
                                        </b-card-body>
                                    </b-col>
                                </b-row>
                            </b-card>
                            <br><br><br>
                        </dd>
                    </dl>
                </b-col>

                <b-col>
                
                </b-col>
            </b-row>
        </b-container>

    </div>

    `
});