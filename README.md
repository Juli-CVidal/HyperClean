# HyperClean
A car wash exercise made as a code challenge for MuniDigital

### If you want to run it,
Execute `launch.sh`, it will create the containers, setup the database and start the backend


## The database
It's a MySQL database and has the following structure:

![The database](docs/images/the_database.png)

| Table | Fields | Relations |
| :--: | --- | --- |
| Customer   | id, name, email, phone                      | -                                |
| Vehicle    | id, license_plate, model, type, customer_id | `N:1` Customer (customer_id)    |
| Appointment | id, appointment_date, status, type, vehicle_id | `N:1` Vehicle (vehicle_id)      |
| Payment    | id, amount, payment_date, type, appointment_id | `1:1` Appointment (appointment_id) |
### Notes:
- The entities inside the application share the same structure.

### Enum Values:
- **VehicleType**: `PICKUP`, `SEDAN`, `SUV`, `SUPERCAR`
- **AppointmentType**: `EXTERIOR`, `INTERIOR`, `COMPLETE`
- **AppointmentStatus**: `PENDING`, `IN_PROGRESS`, `FINISHED`, `PAID`
- **PaymentType**: `CASH`, `CREDIT`, `DEBIT` (Only `CASH` is implemented as required)

The API is divided into four categories, CUSTOMER, VEHICLE, APPOINTMENT, PAYMENT

--------------
# The endpoints
![Postman collection](docs/images/endpoints.png)

> Wanna try it yourself? [Download HyperClean.postman_collection.json](https://github.com/Juli-CVidal/HyperClean/blob/main/docs/HyperClean.postman_collection.json)

# Customer (api/v1/customer)
## POST(/)
 Allows the creation of a customer

### Body Structure
```
{
  "name": "string",
  "email": "string",
  "phone": "string",
}
```
![POST customers](docs/images/customer/post-customer.png)

## GET (/)
Returns all the customers
![GET all](/docs/images/customer/get-all.png)

## GET (/{id})
Searches for a customer based on the id
![GET customer by id](/docs/images/customer/get-by-id.png)

## GET by email (/by-email?email)
Searches for a customer based on the email
![GET customer by email](/docs/images/customer/get-by-email.png)

## GET by phone (/by-phone?phone)
Searches for a customer based on the phone
![GET customer by phone](/docs/images/customer/get-by-phone.png)


# Vehicle (api/v1/vehicle)
## POST(/)
 Allows the creation of a vehicle

### Body Structure
```
{
  "model": "string",
  "licensePlate": "string",
  "customerId": "number",
  "vehicleType": "VehicleType"
}
```
![POST vehicle](docs/images/vehicle/post-vehicle.png)

## GET (/{id})
Searches for a vehicle based on the id
![GET vehicle by id](/docs/images/vehicle/get-by-id.png)

## GET by customer id (/by-customer/{customerId})
Searches for the vehicles of a customer
![GET vehicles by customer id](/docs/images/vehicle/get-by-customer-id.png)

## PUT assign to customer (/{id}/assign-to-customer/{customerId})
Assigns the vehicle to a customer
![PUT assign to customer](/docs/images/vehicle/assign-to-customer.png)


# Appointment (api/v1/appointment)
## POST(/)
 Allows the creation of an appointment

### Body Structure
```
{
  "appointmentDate": "string", // Format dd-MM-yyyy HH:mm:ss
  "status": "AppointmentStatus",
  "serviceType": "ServiceType",
  "vehicleId": "number"
}
```
![POST appointment](docs/images/appointment/post-appointment.png)

## GET (/{id})
Searches for an appointment using id
![GET appointment by id](/docs/images/appointment/get-by-id.png)

## GET by vehicle (/by-vehicle/{vehicleId})
Searches for appointments of a vehicle
![GET appointments by vehicleId](/docs/images/appointment/get-by-vehicle-id.png)

## PUT (/{id}/mark-as-in-progress)
Allows to mark an appointment as in progress
![PUT mark as in progress](/docs/images/appointment/mark-as-in-progress.png)

## PUT (/{id}/mark-as-finished)
Allows to mark an appointment as finished
![PUT mark as in progress](/docs/images/appointment/mark-as-finished.png)

# Payment (api/v1/payment)
## POST(/)
 Allows the creation of a payment

### Body Structure
```
{
  "amount": "number",
  "paymentDate": "String", // Format dd-MM-yyyy HH:mm:ss
  "type": "PaymentType",
  "appointmentId": "number"
}
```
![POST payment](docs/images/payment/post-payment.png)
> Note that this flow also mark the appointment as paid, if applicable

## GET (/{id})
Searches for an appointment using id
![GET payment](docs/images/payment/get-by-id.png)

## GET by appointment id(/by-appointment/{appointmentId})
Searches for an appointment using id
![GET payment](docs/images/payment/get-by-appointment-id.png)
