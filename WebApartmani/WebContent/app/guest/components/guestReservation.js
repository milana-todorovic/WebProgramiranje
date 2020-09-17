Vue.component("guest-reservations", {
    data: function () {
        return {
            name: 'Rezervacije',
            reservations: [],
            reservationSearch: {
                "sort": null
            }

        }
    }
    ,
    methods: {
        searchResByPrice: function () {
            axios.post('rest/reservations/search', {
                "sort": this.reservationSearch.sort
            })
                .then((response) => {
                    this.reservations = [];
                    for (reservation of response.data) {
                        this.reservations.push({
                            res: reservation,
                            comment: null,
                            rating: 5
                        });
                    }
                }
                )
        }, changeStatus: function (reservation) {

            axios({
                method: 'put',
                url: '/WebApartmani/rest/reservations/' + reservation.res.id + "/status",
                data: "\"Otkazana\"",
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then((response) => { reservation.res.status = 'Otkazana' }
            )
        },
        addComment: function (reservation) {

            axios.post('rest/comments', {
                "rating": reservation.rating,
                "text": this.commentAdd.text,
                "apartment": this.commentAdd.apartment
            })
                .then((response) => {
                }
                )
        },
        formatDate(date){
            let realDate = new Date(date);
            realDate.setTime(realDate.getTime() + realDate.getTimezoneOffset()*60*1000);
            return realDate.getDate() + "." + (realDate.getMonth() + 1) + "." + realDate.getFullYear();
        }
    },
    mounted() {

        axios
            .get("rest/reservations")
            .then(response => {
                this.reservations = [];
                for (reservation of response.data) {
                    this.reservations.push({
                        res: reservation,
                        comment: null,
                        rating: 5
                    });
                }

            });




    },
    template: `
        <div>
            <b-container style="margin-left:1%;">
                <b-row>
                    <b-col sm="2" style="margin-left:1%;margin-top:5%;">
                        <div>
                            <b-card >
                                <b><b-form-group label="Sortiranje po ceni"></b>
                                        <b-form-radio v-model="reservationSearch.sort" name="some-radios" value="Rastu\u0107e">Rastu\u0107e</b-form-radio>
                                        <b-form-radio v-model="reservationSearch.sort" name="some-radios" value="Opadaju\u0107e">Opadaju\u0107e</b-form-radio>
                                    </b-form-group>
                                    <br><br>
                                    <b-button @click="searchResByPrice()"  variant="primary"> 
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
                                                        {{reservation.res.apartment.name}}                                                      
                        
                                                    </h1>
                                                    <div style="background-color:teal;padding:5%;color:white;font-size:18px">
                                                        Od  <b>{{realDate(reservation.res.startDate)}}</b>
                                                        <br>
                                                        Broj no\u0107enja  <b>{{reservation.res.numberOfNights}}</b>
                                                        <br>
                                                        Po ceni  <b>{{reservation.res.totalPrice}}</b>
                                                    </div>
                                                </b-col>
                                                
                                                <b-col>
                                                        <h1 style="font-size:30px;margin-top:9%">
                                                            <b-badge variant="success" >{{reservation.res.status}}</b-badge>
                                                        </h1>
                                                    
                                                    <br>
                                                    <b-button  @click="changeStatus(reservation)" v-if="reservation.res.status==='Kreirana' || reservation.res.status==='Prihva\u0107ena'" variant="outline-danger" >
                                                        Odustani
                                                    </b-button>
                                                </b-col>
                                            </b-row>
                                            <br>
                                            

                                            <b-row v-if="reservation.res.status==='Zavr\u0161ena' || reservation.res.status==='Odbijena'">
                                                <b-col>
                                                <hr class="solid" style=" border-top: 1px solid #bbb;">
                                                    <br>
                                                    Va&#x161; komentar:
                                                    
                                                    <b-form-textarea v-model="reservation.text" placeholder="Unesite komentar"></b-form-textarea>
                                                    <br>
                                                    Va&#x161;a ocena:
                                                
                                                    <b-form-rating stars="10" v-model="reservation.rating" show-value precision="1"></b-form-rating>
                                                    <br>

                                                    <b-button @click="addComment(reservation)" variant="outline-primary">
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