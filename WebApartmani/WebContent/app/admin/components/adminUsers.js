Vue.component("admin-users",{
    data: function(){
        return{
            flag:0,
            korisnici:[
                {
                    ime:'Pera',
                    prezime:'Peric',
                    uloga:'gost',
                    korisnickoIme:'pericavrecica888',
                    
                    pol:'muski'
                },
                {
                    ime:'Mika',
                    prezime:'Mikic',
                    uloga:'host',
                    korisnickoIme:'mikicacikica74',
                  
                    pol:'muski'
                },
                {
                    ime:'Zoka',
                    prezime:'Zokic',
                    uloga:'gost',
                    korisnickoIme:'zokicazekica45',
                   
                    pol:'zenski'
                }
            ],
            uloge: ['Gost', 'Doma\u0107in', 'Admin'],
            polovi:['Mu\u0161ki','\u017Denski']
        
        }
       
    },
    template:`
        <div style="font-size:18px;">

            <b-container>

                <b-row>
                    <b-col>
                        <b-card style=" padding: 0.5%;margin-left:1%;margin-top:1%;margin-right:20%;">
                            <b-card-text>
                                <b-form inline>
                                    <b-form-input   placeholder="Korisni\u010Dko ime"></b-form-input>
                                    <b-form-input list="my-list" placeholder="Pol"></b-form-input>
                                        <datalist id="my-list">
                                            <option v-for="pol in polovi">{{ pol }}</option>
                                        </datalist>
                                    <b-form-input list="my-list-id" placeholder="Uloga"></b-form-input>
                                        <datalist id="my-list-id">
                                            <option v-for="uloga in uloge">{{ uloga }}</option>
                                        </datalist>
                                    
                                    <b-button   variant="primary" style="margin-left:2%;">
                                        <b-icon icon="search"></b-icon>
                                        Pretra&#x17E;i
                                    </b-button>
                                </b-form>
                            </b-card-text>
                        </b-card>
                    </b-col>

                </b-row>

                <b-row>
                    <b-col>
                        <dl>
                            <dd v-for="korisnik in korisnici">
                                <b-card style="margin-left:1%;margin-top:5%;margin-right:20%;">
                                    <h1 style="font-size:25px;">{{korisnik.ime}} {{korisnik.prezime}} <b-badge variant="primary">{{korisnik.uloga}}</b-badge></h1>
                                    <label>Korisnicko ime </label><b> {{korisnik.korisnickoIme}}</b>
                                    <br>
                                    <label>Pol</label><b> {{korisnik.pol}}</b>
                                </b-card>

                            </dd>

                        </dl>
                    </b-col>
                
                </b-row>

            </b-container>
        </div>
    
    `

});