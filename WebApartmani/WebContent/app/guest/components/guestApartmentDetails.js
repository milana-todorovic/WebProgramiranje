Vue.component("guest-apartmentDetails",{
    data: function(){
        return{
          name:"Detalji apartmana",
          apartman:[
              {
                  ime:"Apartmani Ivana",
                  tip:"ceo apartman",
                  lokacija:"Pariz"
              }
          ],
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
                        Apartman Ivana <b-badge variant="success">ceo apartman</b-badge>
                        </h1>
                        <br>
                        <b-icon icon="geo-alt" style="width: 25px; height: 25px;"></b-icon>
                        <span style="font-size:20px"> Pariz, Senska ulica broj 84</span>
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
                        <i>2 soba/4 gosta</i>
                        <br>
                        <b>Vreme za prijavu:</b>11:00h 
                        <br>
                        <b>Vreme za odjavu:</b> 10:00h
                        <hr class="solid" style=" border-top: 1px solid #bbb;">
                        <b>Sadr\u017Eaji</b>
                        <div class="wrapper">
                            <ul style="list-style-type:circle;align:left;">
                            
                                <li>Coffee</li>
                                <li>Tea</li>
                                <li>Milk</li>
                                <li>Sapun</li>
                                <li>Tus kabina</li>
                                <li>Ves masina</li>
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
                                            50$ <i>po no\u0107enju</i>
                                        </h1>
                                        <br>
                                        <label><b>Dostupni datumi</label>
                                        <br>
                                        <b-calendar>
                                        </b-calendar>
                                        <br><br>
                                        <label><b>Po\u010Detni datum</label>
                                        <b-form-datepicker   placeholder="Izaberite po\u010Detni datum" class="mb-2"></b-form-datepicker>
                                        <br>
                                        <label><b>Broj no\u0107enja</label>
                                        <b-form-input   placeholder="Unesite broj no\u0107enja"></b-form-input>
                                        <br>
                                        <label><b>Komentar</label>
                                        <b-form-textarea placeholder="Ostavite poruku doma\u0107inu"></b-form-textarea>
                                        <br><br>
                                        <b-button variant="primary">
                                            <b-icon icon="pen"></b-icon>
                                            Rezervi\u0161i apartman
                                        </b-button>
    
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