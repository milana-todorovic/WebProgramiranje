Vue.component("guest-reservations",{
    data: function(){
        return{
            name:'Rezervacije',
            rezervacije:[
                {
                    imeApartmana:'Apartmani Ivana',
                    pocetniDatum:'25.5.2020',
                    krajnjiDatum:'28.10.2020',
                    cenaApartmana:'400$'
                },
                {
                    imeApartmana:'Apartmani Ana',
                    pocetniDatum:'2.8.2020',
                    krajnjiDatum:'28.8.2020',
                    cenaApartmana:'300$'
                },
                {
                    imeApartmana:'Sobe Slobodan Bajic',
                    pocetniDatum:'21.10.2020',
                    krajnjiDatum:'22.10.2020',
                    cenaApartmana:'40$'
                }
            ]

        }
    },
    template:`
        <div>
            <dl>
                <dd v-for="rezervacija in rezervacije">
                <b-card style="max-width: 840px;margin-left:10%;margin-top:5%">
                    <h1 id="nazivApartmana">
                    {{rezervacija.imeApartmana}}

                    </h1>

                    
                    <div style="background-color:teal;width:20%;padding:2%;color:white;margin-top:5%">
                        <i>Od</i> {{rezervacija.pocetniDatum}}
                        <i>do</i>{{rezervacija.krajnjiDatum}}
                        <br>
                        {{rezervacija.cenaApartmana}}
                    </div>

                    
                </b-card>
                </dd>
            </dl>
            
            
        </div>
    `
});