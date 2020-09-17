Vue.component("admin-apartments",{
    data: function(){
        return{
            name: 'Apartmani',
            apartments:[],
            options2:[],
            apartmentSearch:{
                //TODO:srediti datume
                startDate:'',
                endDate:'',
                minimumPrice:'',
                maximumPrice:'',
                minimumNumberOfRooms:'',
                maximumNumberOfRooms:'',
                minimumNumberOfGuests:'',
                maximumNumberOfGuest:'',
                city:'',
                country:'',
                amenities:[],
                types:[],
                status:[]
            },
            options: [
                { text: 'Ceo apartman', value: 'Ceo apartman' },
                { text: 'Soba', value: 'Soba' },
               
              ],
         
        
            options4:[
                {text:'Aktivan',value:'Aktivan'},
                {text:'Neaktivan',value:'Neaktivan'}
            ]
        
        }
    
    },methods: {
        prikaziDetaljeFun: function(apartman){
            alert('radi' + apartman.ime);

            window.location.href = "http://localhost:8081/WebApartmani/admin.html#/apartmentDetails";
        },
        searchAp:function(){

            axios.post('rest/apartments/search', {
                "startDate":this.apartmentSearch.startDate,
                "endDate":this.apartmentSearch.endDate,
                "minimumPrice":this.apartmentSearch.minimumPrice,
                "maximumPrice":this.apartmentSearch.maximumPrice,
                "minimumNumberOfRooms":this.minimumNumberOfRooms,
                "maximumNumberOfRooms":this.maximumNumberOfRooms,
                "minimumNumberOfGuests":this.minimumNumberOfGuests,
                "maximumNumberOfGuests":this.maximumNumberOfGuest,
                "city":this.city,
                "country":this.country,
                "amenities":this.apartmentSearch.amenities,
                "types":this.apartmentSearch.types,
                "status":this.apartmentSearch.status
               
              })
              .then((response) => {
                console.log(response);
                this.apartments=[];
				this.apartments=response.data;
                }
              )
        }
       
    }, mounted(){
		
		axios
		.get("rest/apartments")
		.then(response =>{
			console.log(response.data);
			this.apartments = response.data;


        });

        axios
        .get("rest/amenities")
        .then(response=>{
            console.log(response.data);
            this.amenities=response.data;
            this.options2=[];

            for(let amenity of this.amenities){
                this.options2.push(
                    {
                        text:amenity.name,
                        value:amenity.id,

                    }
                );
            }

            
        })
    },
    template:`
    <div style="margin-right:1%;">
           
            <b-card id="pretraga">
                <b-card-text>
                    <b-form inline>
                    <b-form inline>
                        <b-form-input   placeholder="Grad" v-model="apartmentSearch.city"></b-form-input>
                        <b-form-input   placeholder="Dr\u017Eava" v-model="apartmentSearch.country"></b-form-input>
                        <b-form-input   placeholder="Min osoba" v-model="apartmentSearch.minimumNumberOfGuests"></b-form-input>
                        <b-form-input   placeholder="Max osoba" v-model="apartmentSearch.maximumNumberOfGuests"></b-form-input>
                        <b-form-input   placeholder="Min soba" v-model="apartmentSearch.minimumNumberOfRooms"></b-form-input>
                        <b-form-input   placeholder="Max soba" v-model="apartmentSearch.maximumNumberOfRooms"></b-form-input>
                        <b-form-input   placeholder="Min cena" v-model="apartmentSearch.minimumPrice"></b-form-input>
                        <b-form-input   placeholder="Max cena" v-model="apartmentSearch.maximumPrice"></b-form-input>
                        <b-form-datepicker  placeholder="Po\u010Detni datum" v-model="apartmentSearch.startDate"></b-form-datepicker>
                        <b-form-datepicker  placeholder="Krajnji datum" v-model="apartmentSearch.endDate"></b-form-datepicker>

                        <b-button  @click="searchAp()"    variant="primary" style="margin-left:2%;">
                            <b-icon icon="search"></b-icon>
                            Pretra&#x17E;i
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
                                    <b-form-radio name="some-radios" value="rastuce">Rastu\u0107e</b-form-radio>
                                    <b-form-radio name="some-radios" value="opadajuce">Opadaju\u0107e</b-form-radio>
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
                                    v-model="apartmentSearch.types"
                                    ></b-form-checkbox-group>
                                </b-form-group>

                                <br><br>

                                <b><b-form-group label="Sadr\u017Eaji"></b>
                                    <b-form-checkbox-group
                                        :options="options2"
                                        plain
                                        stacked
                                        v-model="apartmentSearch.amenities"
                                        ></b-form-checkbox-group>
                                    </b-form-group>
                            
                                <br><br>

                                <b><b-form-group label="Status"></b>
                                <b-form-checkbox-group
                                    :options="options4"
                                    plain
                                    stacked
                                    v-model="apartmentSearch.status"
                                    ></b-form-checkbox-group>
                                </b-form-group>
                        
                            <br><br>

                                <b-button @click="searchAp()"  variant="primary">
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
                            <dd v-for="apartment in apartments">
                
                                <b-card style="max-width: 740px;margin-left:10%" no-body>
                    
                                    <b-row no-gutters>
                                        <b-col>
                                            <b-card-img src="https://picsum.photos/400/400/?image=20" 			alt="Image" class="rounded-0"></b-card-img>
                                        </b-col>
                                        
                                        <b-col>
                                            <b-card-body>
                                                <b-card-text>
                                                    <h1 id="nazivApartmana">
                                                    {{apartment.name}} <b-badge variant="success">{{apartment.apartmentType}}</b-badge>
                                                    </h1>

                                                    

                                                    <br>
                                                    <b-icon icon="geo-alt" style="width: 25px; height: 25px;">
                                                    </b-icon>

                                                    <span style="font-size:20px">
                                                        {{apartment.location.address.street}} {{apartment.location.address.number}}
                                                    </span>
                                                  
                                                    <br>
                                                    <span style="font-size:25px">
                                                      <b>{{apartment.pricePerNight}}</b>
                                                    </span>
                                                    
                                                        <i>po no&#x107;enju</i>
                                                   
                                                    <br><br><br><br><br>
                                                    <b-button variant="primary" @click="prikaziDetaljeFun(apartman)">
                                                        Prika&#x17E;i detalje
                                                        <b-icon icon="arrow-right">
                                                    </b-button>
                                                    <br><br>
                                                    <b-button variant="danger">
                                                        <b-icon icon="x"></b-icon>
                                                        Ukloni apartman
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