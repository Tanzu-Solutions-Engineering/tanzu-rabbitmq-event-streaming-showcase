using System.Text;
using Imani.Solutions.Core.API.Util;
using RabbitMQ.Client;


var config = new ConfigSettings();
var clientName = config.GetProperty("clientName","Send");
var routingKeyValue = config.GetProperty("routingKey","hello-quorum");
var exchangeName = config.GetProperty("exchange","");
var message = config.GetProperty("message","Hello World!");
var body = Encoding.UTF8.GetBytes(message);


var factory = new ConnectionFactory { Uri = new Uri("amqp://user:bitnami@localhost"), 
ClientProvidedName = clientName};
using var connection = factory.CreateConnection();
using var channel = connection.CreateModel();


channel.BasicPublish(exchange: exchangeName,
                     routingKey: routingKeyValue,
                     basicProperties: null,
                     body: body);
Console.WriteLine($" [x] Sent {message}");

Console.WriteLine(" Press [enter] to exit.");
Console.ReadLine();


