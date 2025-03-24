using System;
using HospitalAppointmentShedule.Domain.IRepository;
using HospitalAppointmentShedule.Infrastructure.Repository;
using HospitalAppointmentShedule.Server;
using Microsoft.EntityFrameworkCore;
using HospitalAppointmentShedule.Domain.IUnitOfWork;
using HospitalAppointmentShedule.Infrastructure.UnitOfWork;
using HospitalAppointmentShedule.Services.IService;
using HospitalAppointmentShedule.Services.Services;
using HospitalAppointmentShedule.Domain.Models;
using AutoMapper;
using HospitalAppointmentShedule.Infrastructure.DBContext;
using HospitalAppointmentShedule.Services.Profile;


var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();


// Add DbContext
builder.Services.AddDbContext<AppointmentSchedulingDbContext>(options =>
   options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));

// Add AutoMapper
builder.Services.AddAutoMapper(typeof(ServiceProfile).Assembly);

// Add Repositories
builder.Services.AddScoped<IUnitOfWork, UnitOfWork>();
builder.Services.AddScoped<IUserRepository, UserRepository>();
builder.Services.AddScoped<IServiceRepository, ServiceRepository>();
builder.Services.AddScoped<IFeedbackRepository, FeedbackRepository>();
builder.Services.AddScoped<IAppointmentRepository, AppointmentRepository>();

// Add Services
builder.Services.AddScoped<IServiceService, ServiceService>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
