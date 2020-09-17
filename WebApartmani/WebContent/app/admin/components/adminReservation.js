Vue.component("admin-reservations",{
    data: function(){
        return{
            reservations:[],
            userSearch:{
                "username":''
            }
           
            
        }
    },
    methods:{
        searchResByUsers:function(){
            

            axios.post('rest/reservations/search', {
                "username":this.reservations.username
               
              })
              .then((response) => {
                console.log(response);
                this.reservations=[];
				this.reservations=response.data;
                }
              )
        }

    },
     mounted(){
		
		axios
		.get("rest/reservations")
		.then(response =>{
            console.log(response.data);
            this.reservations=[];
            this.reservations=response.data;

        });



        
       

    
	},
    template:`
       
        <div>
            <b-container style="margin-left:1%;">
                <b-row>
                    <b-col>
                        <b-card style=" padding: 0.5%;margin-left:1%;margin-top:1%;margin-right:6%;">
                            <b-card-text>
                                <b-form inline>
                                    <b-form-input v-model="userSearch.username"  placeholder="Korisni\u010Dko ime gosta"></b-form-input>
                                    
                                    
                                    <b-button @click="searchResByUsers()"  variant="primary" style="margin-left:2%;">
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
                                <dd v-for="reservation in reservations">
                                    <b-card style="max-width: 840px;">
                                        

                                        <b-container>
                                            <b-row>
                                                <b-col>
                                                    <h1 id="nazivApartmana">
                                                        <a href= "http://localhost:8081/WebApartmani/admin.html#/apartmentDetails" style="color:black;">
                                                        {{reservation.apartment.name}}
                                                        </a>
                                                    </h1>
                                                
                                                
                                                    <div style="background-color:teal;padding:5%;color:white;font-size:18px">
                                                        Gost <b>{{reservation.guest.name}} {{reservation.guest.surname}}</b>
                                                        <br>
                                                        Od  <b>{{reservation.startDate}}</b>
                                                        <br>
                                                        Broj no\u0107enja <b>{{reservation.numberOfNights}}</b>
                                                        <br>
                                                        Po ceni  <b>{{reservation.totalPrice}}</b>
                                                    </div>
                                                </b-col>
                                                
                                                <b-col>
                                                        <h1 style="font-size:30px;margin-top:9%">
                                                            <b-badge variant="success" >{{reservation.status}}</b-badge>
                                                        </h1>
                                                    
                                                    
                                                   
                                                </b-col>
                                            </b-row>
                                            <br>
                                            <hr class="solid" style=" border-top: 1px solid #bbb;">

                                            <b-row>
                                                <b-col>
                                                
                                                <b>Poruka koju je ostavio gost</b>
                                                <br>
                                                <div style="font-size:18px;">
                                                        {{reservation.message}}
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