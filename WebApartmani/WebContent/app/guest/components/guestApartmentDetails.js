Vue.component("guest-apartmentDetails",{
    data: function(){
        return{
          name:"Detalji apartmana",
          apartment: null,
          comments:[],
          images: [],
          globalAlert: { show: false, text: null },
          date: null,
          numberOfDays: null,
          guestComment: null
        }
    },
    created () {
        this.fetchData()
      },
      watch: {
        '$route': 'fetchData'
      },
      computed:{
    	  address: function() {
    		  let addr = '';
    		  if (this.apartment.location.address.street)
    			  addr += street;
    		  if (this.apartment.location.address.number)
    			  addr += ' ' + this.apartment.location.address.number;
    		  if (this.apartment.location.address.city)
    			  addr += ', ' + this.apartment.location.address.city;
    		  if (this.apartment.location.address.postalCode)
    			  addr += ' ' + this.apartment.location.address.postalCode;
    		  if (this.apartment.location.address.country)
    			  addr += ', ' + this.apartment.location.address.country;
    		  return addr;
    	  }
      },
      methods:{
    	  fetchData(){
    		  this.apartment = null;    		  
    		  axios.get("/WebApartmani/rest/apartments/" + this.$route.params.id).then(
    				  response => this.apartmentLoaded(response.data)
    				  ).catch(error => {this.setGlobalAlert(error.response.data)});
    	  },
    	  apartmentLoaded(apartment){
    		  if (apartment.id == this.$route.params.id) {
    			  let fixedDates = [];
    	            for(date of apartment.availableDates){
    	            	let realDate = new Date(date);
    	                fixedDates.push(new Date(realDate.getTime() + realDate.getTimezoneOffset()*60*1000));
    	            }
    	           apartment.availableDates = fixedDates;
    	           this.numberOfDays = 1;
    	           this.date = null;
    	           this.guestComment = null;
    			  this.apartment = apartment;
    			  axios.get("/WebApartmani/rest/comments", {
    					params: { apartment: apartment.id }
    				}).then(response => this.commentsLoaded(apartment, response.data)).catch(
    						error => {if (this.$route.params.id == apartment.id) this.setGlobalAlert("Nije uspelo u\u010Ditavanje komentara."); console.log(error)});
    		  }
    	  },
    	  commentsLoaded(apartment, comments){
    		  if (apartment.id == this.$route.params.id){
    			  this.comments = comments;
    			  axios.get("/WebApartmani/rest/apartments/" + apartment.id + "/images").then(response => this.allLoaded(apartment, response.data)).catch(
  						error => {if (this.$route.params.id == apartment.id) this.setGlobalAlert("Nije uspelo u\u010Ditavanje slika."); console.log(error)});
    		  }
    	  },
    	  allLoaded(apartment, images) {
    		  if (apartment.id == this.$route.params.id){
    			  this.images = images;
    		  }
    	  },
    	  submit(event){
    		  event.preventDefault();
    		  let error = '';
    		  let hasError = false;
    		  if (!this.date){
    			  error += "Mora biti izabran datum po\u010Detka. ";
    			  hasError = true;
    		  }
    		  if (!this.numberOfDays){
    			  error += "Mora biti izabran broj dana. ";
    			  hasError = true;
    		  } else if (! /^[1-9][0-9]*$/.test(this.numberOfDays)){
    			  error += "Broj dana mora biti broj ve\u0107i od 1. ";
    			  hasError = true;
    		  }
    		  
    		  if (hasError){
    			  this.setGlobalAlert(error);
    			  return;
    		  }
    		  
    		  axios.post("/WebApartmani/rest/reservations", 
    				  { apartment: {id: this.apartment.id},
    			  startDate: this.date.getTime() - this.date.getTimezoneOffset()*60*1000, 
    			  numberOfNights: this.numberOfDays, 
    			  message: this.guestComment}).then(
    					  response => this.setGlobalAlert("Uspe\u0161no je kreirana rezervacija.")
    					  ).catch(
    							  error => {this.setGlobalAlert("Nije uspelo kreiranje rezervacije: " + error.response.data)}  );
    	  },
    	  setGlobalAlert(text) {
    	      this.globalAlert.text = text;
    	      this.globalAlert.show = true;
    	    }
      },
    template:`
       <div>
    		<b-alert
    		v-model="globalAlert.show"
    		dismissible>
    		{{ globalAlert.text }}
    		</b-alert>
       
            <b-container style="margin-top:5%;" v-if="apartment">
                <b-row>
                    <b-col cols="8">
                        <h1 id="nazivApartmana">
                        {{ apartment.name }} <b-badge variant="success">{{apartment.apartmentType}}</b-badge>
                        </h1>
                        <br>
                        <b-icon icon="geo-alt" style="width: 25px; height: 25px;"></b-icon>
                        <span style="font-size:20px">{{address}}</span>
                        <br><br>
                        	<b-carousel v-if="images.length>0"
                            controls
                            indicators
                            img-width="1024"
                            img-height="480"
                            style="text-shadow: 1px 1px 2px #333;"
                            >
                            
                            <b-carousel-slide v-for="image in images" v-bind:img-src="image.data"
                            ></b-carousel-slide>
                            </b-carousel>
                        <br>
                        <i>{{apartment.numberOfRooms}} soba/{{apartment.numberOfGuests}} gostiju</i>
                        <br>
                        <b>Vreme za prijavu:</b> {{ apartment.checkInTime}}
                        <br>
                        <b>Vreme za odjavu:</b> {{apartment.checkOutTime}}
                        <hr class="solid" style=" border-top: 1px solid #bbb;">
                        <b>Sadr\u017Eaji</b>
                        <div>
                            <ul style="list-style-type:circle;align:left;">
                            
                                <li v-for="amenity in apartment.amenities" >
                                    {{ amenity.name }}
                                </li>

                            </ul>  
                        </div>
                        
                       
                        <hr class="solid" style=" border-top: 1px solid #bbb;">
                        <b>Komentari i ocene</b>
                        <dl>
                            <dd v-for="comment in comments">
                                <b-card>
                                    <b-container>
                                        <b-row>
                                            
                                            <b-col cols="10">
                                                <i>{{comment.guest.name + ' ' + comment.guest.surname}}</i>
                                                <br>
                                                {{comment.text}}
                                            </b-col>
                                            <b-col>
                                                <h1 style="font-size:35px;">
                                                    <b-badge variant="success">
                                                    {{comment.rating}}</b-badge>
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
                                            {{apartment.pricePerNight}}$ <i>po no\u0107enju</i>
                                        </h1>
                                        <br>
                                        
                                          <b-form v-on:submit="submit">
     
    <b-form-group
        label="Datum po\u010Detka:"
        >
        <v-date-picker
    	v-model='date'
    	:available-dates="apartment.availableDates"/>
      </b-form-group>
      
      <b-form-group
        label="Broj no\u0107enja:"
        >
        <b-form-input
          v-model="numberOfDays"
          type="text"
          placeholder="Unesite broj no\u0107enja"
        ></b-form-input>
      </b-form-group>  
      
    <b-form-group
        label="Poruka za doma\u0107ina:"
        >
        <b-form-input
          v-model="guestComment"
          type="text"
          placeholder="Unesite poruku"
        ></b-form-input>
      </b-form-group>                  
      
      <b-button class="mt-2 mb-2" type="submit" variant="primary">Rezervi\u0161i</b-button>
    
    </b-form> 
    
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