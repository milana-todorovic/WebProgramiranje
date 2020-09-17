Vue.component("admin-apartmentDetails",{
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
                    
                </b-row>
            </b-container>
           

            
       </div>
           
    `

});