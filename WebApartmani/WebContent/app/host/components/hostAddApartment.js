Vue.component("host-addApartment",{
    data: function(){
        return{
            flag:0
        }
    },
    template:`
        <div>
            <b-container>
                <b-row>
                    <b-col>
                        <b-card style="max-width:680px;margin-top:5%;">
                            <h1>Dodavanje novog apartmana</h1>

                            <br>
                            <b><label>Ime apartmana</label></b>
                            <b-form-input placeholder="Unesite ime apartmana"></b-form-input>
                            <br>

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
                            
                            <b><label>Cena po no\u0107enju</label></b>
                                <b-form-input placeholder="Unesite cenu po no\u0107enju"></b-form-input>
                            <br>
                                            
                            <b><label>Vreme za prijavu</label></b>
                                <b-form-input placeholder="Unesite vreme za prijavu"></b-form-input>
                            <br>
                                            
                            <b><label>Vreme za odjavu</label></b>
                                <b-form-input placeholder="Unesite vreme za odjavu"></b-form-input>
                            <br>

                            <br>

                            <b-button variant="primary">
                                <b-icon icon="plus-circle-fill"></b-icon>
                                Dodaj
                            </b-button>
                        </b-card>

                        

                    <br>
                    </b-col>
                </b-row>
            </b-container>
         
        </div>
    `,
    methods:{
        logout: function(event){
            event.preventDefault;
            alert("Simulacija odjave");
        }
    }

});