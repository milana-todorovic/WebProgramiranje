const ApartmentComponent={template: '<guest-apartments></guest-apartments>'}
const ReservationComponent={template: '<guest-reservations></guest-reservations>'}
const ApartmentDetailsComponent={template: '<guest-apartmentDetails></guest-apartmentDetails>'}
const ProfileComponent={template: '<guest-profile></guest-profile>'}
const HostComponent={template: '<guest-host></guest-host>'}
const HostApartmentComponent={template:'<guest-hostAp></guest-hostAp>'}
const HostApartmentDetailsComponent={template:'<guest-hostApDetails></guest-hostApDetails>'}
const HostUsersComponent={template:'<guest-hostUsers></guest-hostUsers>'}
const AdminApartmentComponent={template:'<guest-adminAp></guest-adminAp>'}
const AdminApartmentDetailsComponent={template:'<guest-adminApDetails></guest-adminApDetails>'}
const AdminUsersComponent={template:'<guest-adminUsers></guest-adminUsers>'}
const AdminReservation={template:'<guest-admin></guest-admin>'}

const router=new VueRouter({
    mode: 'hash',
    routes:[
        {path: '/apartments', component:ApartmentComponent},
        {path: '/reservations', component:ReservationComponent},
        {path: '/apartmentDetails', component:ApartmentDetailsComponent},
        {path: '/profile', component:ProfileComponent},
        {path: '/host',component:HostComponent},
        {path:'/hostAp',component:HostApartmentComponent},
        {path:'/hostApDetails',component:HostApartmentDetailsComponent},
        {path:'/hostUsers',component:HostUsersComponent},
        {path:'/adminAp',component:AdminApartmentComponent},
        {path:'/adminApDetails',component:AdminApartmentDetailsComponent},
        {path:'/adminUsers',component:AdminUsersComponent},
        {path:'/admin',component:AdminReservation}
    ]
});

var appGuest = new Vue({
    router,
	el : "#guestApp",
	data : {
		title : "Guest naslov"
    },
	
});