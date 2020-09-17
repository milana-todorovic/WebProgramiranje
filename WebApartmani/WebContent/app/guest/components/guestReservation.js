Vue.component("guest-reservations",{
    data: function(){
        return{
            name:'Rezervacije',
            reservations:[],


           

        }
    }
    , mounted(){
		
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
                    <b-col sm="2" style="margin-left:1%;margin-top:5%;">
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
                    </b-col>

                    <b-col>
                        <div>
                            <dl>
                                <dd v-for="reservation in reservations">
                                    <b-card style="max-width: 840px;margin-top:6%;">
                                        

                                        <b-container>
                                            <b-row>
                                                <b-col>
                                                    <h1 id="nazivApartmana">
                                                        <a href= "http://localhost:8081/WebApartmani/guest.html#/apartmentDetails" style="color:black;">
                                                        {{reservation.apartment.name}}
                                                        </a>
                        
                                                    </h1>
                                                    <div style="background-color:teal;padding:5%;color:white;font-size:18px">
                                                        Od  <b>{{reservation.startDate}}</b>
                                                        <br>
                                                        Broj no\u0107enja  <b>{{reservation.numberOfNights}}</b>
                                                        <br>
                                                        Po ceni  <b>{{reservation.totalPrice}}</b>
                                                    </div>
                                                </b-col>
                                                
                                                <b-col>
                                                        <h1 style="font-size:30px;margin-top:9%">
                                                            <b-badge variant="success" >{{reservation.status}}</b-badge>
                                                        </h1>
                                                    
                                                    <br>
                                                    <b-button variant="outline-danger" @click="reservation.status='odustanak' ">
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