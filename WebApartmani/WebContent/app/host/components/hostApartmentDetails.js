Vue.component("host-apartmentDetails",{
    data: function(){
        return{
          name:"Detalji apartmana",
          apartman:{
            ime:"Apartmani Ivana",
            tip:"ceo apartman",
            lokacija:"Pariz,Senska ulica broj 84",
            brojGostiju:"4",
            brojSoba:"2",
            vremeZaPrijavu:"13:00h",
            vremeZaOdjavu:"10:00h",
            sadrzaj:[
                {
                    ime: "Krevet",
                },
                {
                    ime: "Kada",
                },
                {
                    ime: "Pegla",
                },
                {
                    ime: "Ogledalo",
                }
            ]
          },
          komentari:[
              {
                  koment:"Apartman je izvrstan. Sve je cisto i uredno. Ljubazni domacini",
                  ocena:"10",
                  gost:"Pera Peric"
              },
              {
                koment:"sve pohvale. Doci cemo ponovo!!!",
                ocena:"10",
                gost:"Jovo Jovic"
            }
          ]
         
        }
    },
    template:`
       <div>
            <b-container style="margin-top:5%;">
                <b-row>
                    <b-col cols="8">
                        <h1 id="nazivApartmana">
                        {{ apartman.ime }} <b-badge variant="success">{{apartman.tip}}</b-badge>
                        </h1>
                        <br>
                        <b-icon icon="geo-alt" style="width: 25px; height: 25px;"></b-icon>
                        <span style="font-size:20px">{{apartman.lokacija}}</span>
                        <br><br>
                        <b-carousel
                            controls
                            indicators
                            img-width="1024"
                            img-height="480"
                            style="text-shadow: 1px 1px 2px #333;"
                            >
                            <b-carousel-slide img-src="https://picsum.photos/1024/480/?image=52"
                            ></b-carousel-slide>
                               
                            <b-carousel-slide img-src="https://picsum.photos/1024/480/?image=58"></b-carousel-slide>
                        </b-carousel>
                        <br>
                        <i>{{apartman.brojSoba}} soba/{{apartman.brojGostiju}} gosta</i>
                        <br>
                        <b>Vreme za prijavu:</b> {{ apartman.vremeZaPrijavu}}
                        <br>
                        <b>Vreme za odjavu:</b> {{apartman.vremeZaOdjavu}}
                        <hr class="solid" style=" border-top: 1px solid #bbb;">
                        <b>Sadr\u017Eaji</b>
                        <div>
                            <ul style="list-style-type:circle;align:left;">
                            
                                <li v-for="jedanSadrzaj in apartman.sadrzaj" >
                                    {{ jedanSadrzaj.ime }}
                                </li>

                            </ul>  
                        </div>
                        
                       
                        <hr class="solid" style=" border-top: 1px solid #bbb;">
                        <b>Komentari i ocene</b>
                        <dl>
                            <dd v-for="komentar in komentari">
                                <b-card>
                                    <b-container>
                                        <b-row>
                                            
                                            <b-col cols="10">
                                                <i>{{komentar.gost}}</i>
                                                <br>
                                                {{komentar.koment}}
                                                <br><br>
                                                <b-button variant="primary">
                                                    <b-icon icon="eye-slash-fill"></b-icon>
                                                        Sakrij komentar
                                                </b-button>
                                            </b-col>
                                            <b-col>
                                                <h1 style="font-size:35px;">
                                                    <b-badge variant="success">
                                                    {{komentar.ocena}}</b-badge>
                                                </h1>
                                            </b-col>
                                        </b-row>
                                    
                                       
                                         
                             
                    
                                      
                                    </b-container>
                                </b-card>
                            </dd>
                        </dl>
                        
                            
                    </b-col>
                    <b-col>
                        <b-container>
                            <b-col>
                                <b-row>
                                    <b-card>
                                        <h1 id="nazivApartmana">
                                          Menjanje podataka
                                        </h1>
                                        <v-date-picker
                                            mode="multiple"
                                            is-inline
                                        />
                                        <b><label>Tip apartmana</label></b>
                                            <b-form-radio name="some-radios" value="ceo">Ceo</b-form-radio>
                                            <b-form-radio name="some-radios" value="soba">Soba</b-form-radio>
                                        <br>
                                        <b><label>Broj soba</label></b>
                                            <b-form-input placeholder="Unesite broj soba"></b-form-input>
                                        <br>
                                        <b><label>Broj osoba</label></b>
                                            <b-form-input placeholder="Unesite broj gostiju"></b-form-input>
                                        <br>
                                        <b><label>Lokacija</label></b>
                                            <b-form-input placeholder="Unesite lokaciju"></b-form-input>
                                        <br>
                                        <b><label>Cena po nocenju</label></b>
                                            <b-form-input placeholder="Unesite cenu po nocenju"></b-form-input>
                                        <br>
                                        <b><label>Vreme za prijavu</label></b>
                                            <b-form-input placeholder="Unesite vreme za prijavu"></b-form-input>
                                        <br>
                                         <b><label>Vreme za odjavu</label></b>
                                            <b-form-input placeholder="Unesite vreme za odjavu"></b-form-input>
                                        <br>
                                         <b><label>Status apartmana</label></b>
                                            <b-form-radio name="some-radios" value="aktivan">Aktivan</b-form-radio>
                                            <b-form-radio name="some-radios" value="neaktivan">Neaktivan</b-form-radio>
                                        <br>

                                    </b-card>
                                </b-row>
                            </b-col>
                        
                        </b-container>
                    
                    </b-col>
                </b-row>
            </b-container>
   

    
        </div>
           
    `

});