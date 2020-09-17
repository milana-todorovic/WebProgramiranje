Vue.component("guest-apartmentDetails",{
    data: function(){
        return{
          name:"Detalji apartmana",
          apartment: null,
          comments:[],
          images: [],
          globalAlert: { show: false, text: null },
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
    		  return this.apartment.location.address.street + ' ' + this.apartment.location.address.number 
    		  + ', ' + this.apartment.location.address.city + ' ' + this.apartment.location.address.postalCode
    		  + ', ' + this.apartment.location.address.country;
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
    			  this.apartment = apartment;
    			  axios.get("/WebApartmani/rest/comments", {
    					params: { apartment: apartment.id }
    				}).then(response => this.commentsLoaded(apartment, response.data)).catch(
    						error => {if (this.$route.params.id == apartment.id) this.setGlobalAlert("Nije uspelo učitavanje komentara."); console.log(error)});
    		  }
    	  },
    	  commentsLoaded(apartment, comments){
    		  if (apartment.id == this.$route.params.id){
    			  this.comments = comments;
    			  axios.get("/WebApartmani/rest/apartments/" + apartment.id + "/images").then(response => this.allLoaded(apartment, response.data)).catch(
  						error => {if (this.$route.params.id == apartment.id) this.setGlobalAlert("Nije uspelo učitavanje slika."); console.log(error)});
    		  }
    	  },
    	  allLoaded(apartment, images) {
    		  if (apartment.id == this.$route.params.id){
    			  this.images = images;
    		  }
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
                            
                            <b-carousel-slide v-for="image in images" img-src="image.data"
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