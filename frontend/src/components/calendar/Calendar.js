import React, { useState } from "react";
import "../../styles/components.css";

const Calendar = ({ events: propEvents = [], onEventClick }) => {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [viewMode, setViewMode] = useState("month");

  // Sample events
  const sampleEvents = [
    {
      id: 1,
      name: "Wedding Reception - Smith Family",
      date: new Date().toISOString().split("T")[0], // Today
      time: "14:00",
      venue: { name: "Grand Ballroom" },
      notes:
        "Black tie event, 200 guests expected, full catering service required",
      client: { name: "Sarah Smith" },
    },
    {
      id: 2,
      name: "Corporate Conference - Tech Summit",
      date: new Date(new Date().setDate(new Date().getDate() + 2))
        .toISOString()
        .split("T")[0], // 2 days from now
      time: "09:00",
      venue: { name: "Convention Center" },
      notes:
        "Annual tech conference, 500 attendees, multiple breakout sessions",
      client: { name: "TechCorp Inc." },
    },
    {
      id: 3,
      name: "Birthday Celebration - Johnson",
      date: new Date(new Date().setDate(new Date().getDate() + 1))
        .toISOString()
        .split("T")[0], // Tomorrow
      time: "18:30",
      venue: { name: "Garden Terrace" },
      notes:
        "Surprise 50th birthday party, live band booked, special dietary requirements",
      client: { name: "Mike Johnson" },
    },
  ];

  // Use sample events if no events are provided
  const events = propEvents.length > 0 ? propEvents : sampleEvents;

  const getDaysInMonth = (date) => {
    return new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate();
  };

  const getFirstDayOfMonth = (date) => {
    return new Date(date.getFullYear(), date.getMonth(), 1).getDay();
  };

  const getEventsForDate = (date) => {
    return events.filter((event) => {
      const eventDate = new Date(event.date);
      return eventDate.toDateString() === date.toDateString();
    });
  };

  const handlePrevious = () => {
    const newDate = new Date(currentDate);
    switch (viewMode) {
      case "month":
        newDate.setMonth(currentDate.getMonth() - 1);
        break;
      case "week":
        newDate.setDate(currentDate.getDate() - 7);
        break;
      case "day":
        newDate.setDate(currentDate.getDate() - 1);
        break;
    }
    setCurrentDate(newDate);
  };

  const handleNext = () => {
    const newDate = new Date(currentDate);
    switch (viewMode) {
      case "month":
        newDate.setMonth(currentDate.getMonth() + 1);
        break;
      case "week":
        newDate.setDate(currentDate.getDate() + 7);
        break;
      case "day":
        newDate.setDate(currentDate.getDate() + 1);
        break;
    }
    setCurrentDate(newDate);
  };

  const getWeekDates = () => {
    const dates = [];
    const startOfWeek = new Date(currentDate);
    startOfWeek.setDate(currentDate.getDate() - currentDate.getDay());

    for (let i = 0; i < 7; i++) {
      const date = new Date(startOfWeek);
      date.setDate(startOfWeek.getDate() + i);
      dates.push(date);
    }
    return dates;
  };

  const getTimeSlots = () => {
    const slots = [];
    for (let hour = 0; hour < 24; hour++) {
      slots.push(`${hour.toString().padStart(2, "0")}:00`);
    }
    return slots;
  };

  const renderMonthView = () => {
    const daysInMonth = getDaysInMonth(currentDate);
    const firstDay = getFirstDayOfMonth(currentDate);
    const days = [];

    // Add weekday headers
    const weekdays = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
    const weekdayHeaders = (
      <div className="calendar-weekdays">
        {weekdays.map((day, index) => (
          <div key={index} className="calendar-weekday">
            {day}
          </div>
        ))}
      </div>
    );

    // Add empty cells for days before the first day of the month
    for (let i = 0; i < firstDay; i++) {
      days.push(<div key={`empty-${i}`} className="calendar-day empty" />);
    }

    // Add days of the month
    for (let day = 1; day <= daysInMonth; day++) {
      const date = new Date(
        currentDate.getFullYear(),
        currentDate.getMonth(),
        day
      );
      const dayEvents = getEventsForDate(date);

      days.push(
        <div key={day} className="calendar-day">
          <div className="calendar-day-number">{day}</div>
          <div className="calendar-day-events">
            {dayEvents.map((event) => (
              <div
                key={event.id}
                className="calendar-event"
                onClick={() => onEventClick(event)}
                title={`${event.time} - ${event.name}`}
              >
                <div className="event-name">{event.name}</div>
              </div>
            ))}
          </div>
        </div>
      );
    }

    // Add empty cells for remaining days to complete the grid
    const totalDays = firstDay + daysInMonth;
    const remainingDays = Math.ceil(totalDays / 7) * 7 - totalDays;
    for (let i = 0; i < remainingDays; i++) {
      days.push(<div key={`empty-end-${i}`} className="calendar-day empty" />);
    }

    return (
      <>
        {weekdayHeaders}
        <div className="calendar-grid">{days}</div>
      </>
    );
  };

  const renderWeekView = () => {
    const weekDates = getWeekDates();

    return (
      <div className="calendar-week-view">
        <div className="calendar-week-header">
          {weekDates.map((date, index) => (
            <div key={index} className="calendar-week-day">
              <div className="calendar-week-day-name">
                {date.toLocaleDateString("en-US", { weekday: "short" })}
              </div>
              <div className="calendar-week-day-number">{date.getDate()}</div>
            </div>
          ))}
        </div>
        <div className="calendar-week-grid">
          {weekDates.map((date, dateIndex) => {
            const dayEvents = getEventsForDate(date);
            return (
              <div key={dateIndex} className="calendar-week-day-column">
                {dayEvents.map((event) => (
                  <div
                    key={event.id}
                    className="calendar-event week-event"
                    onClick={() => onEventClick(event)}
                  >
                    <div className="event-time">{event.time}</div>
                    <div className="event-name">{event.name}</div>
                    {event.venue && (
                      <div className="event-venue">📍 {event.venue.name}</div>
                    )}
                    {event.client && (
                      <div className="event-venue">👤 {event.client.name}</div>
                    )}
                  </div>
                ))}
              </div>
            );
          })}
        </div>
      </div>
    );
  };

  const renderDayView = () => {
    const timeSlots = getTimeSlots();
    const dayEvents = getEventsForDate(currentDate);

    return (
      <div className="calendar-day-view">
        <div className="calendar-day-header">
          <div className="calendar-day-title">
            {currentDate.toLocaleDateString("en-US", {
              weekday: "long",
              month: "long",
              day: "numeric",
            })}
          </div>
        </div>
        <div className="calendar-day-timeline">
          {timeSlots.map((time, index) => (
            <React.Fragment key={index}>
              <div className="calendar-time">{time}</div>
              <div className="calendar-slot-content">
                {dayEvents
                  .filter(
                    (event) => event.time.split(":")[0] === time.split(":")[0]
                  )
                  .map((event) => (
                    <div
                      key={event.id}
                      className="calendar-event day-event"
                      onClick={() => onEventClick(event)}
                    >
                      <div className="event-time">{event.time}</div>
                      <div className="event-name">{event.name}</div>
                      {event.venue && (
                        <div className="event-venue">📍 {event.venue.name}</div>
                      )}
                    </div>
                  ))}
              </div>
            </React.Fragment>
          ))}
        </div>
      </div>
    );
  };

  const getViewTitle = () => {
    const options = { month: "long", year: "numeric" };
    switch (viewMode) {
      case "month":
        return currentDate.toLocaleDateString("default", options);
      case "week":
        const weekDates = getWeekDates();
        const firstDay = weekDates[0].toLocaleDateString("default", {
          month: "long",
          day: "numeric",
        });
        const lastDay = weekDates[6].toLocaleDateString("default", {
          month: "long",
          day: "numeric",
          year: "numeric",
        });
        return `${firstDay} - ${lastDay}`;
      case "day":
        return currentDate.toLocaleDateString("default", {
          ...options,
          day: "numeric",
        });
      default:
        return "";
    }
  };

  return (
    <div className="calendar-container">
      <div className="calendar-header">
        <div className="flex">
          <button className="button button-outline" onClick={handlePrevious}>
            &lt;
          </button>
          <h2 className="calendar-title">{getViewTitle()}</h2>
          <button className="button button-outline" onClick={handleNext}>
            &gt;
          </button>
        </div>
        <div className="flex" style={{ gap: "0.5rem" }}>
          <button
            className={`button ${
              viewMode === "month" ? "button-primary" : "button-outline"
            }`}
            onClick={() => setViewMode("month")}
          >
            Month
          </button>
          <button
            className={`button ${
              viewMode === "week" ? "button-primary" : "button-outline"
            }`}
            onClick={() => setViewMode("week")}
          >
            Week
          </button>
          <button
            className={`button ${
              viewMode === "day" ? "button-primary" : "button-outline"
            }`}
            onClick={() => setViewMode("day")}
          >
            Day
          </button>
        </div>
      </div>

      {viewMode === "month" && (
        <div className="calendar-month-view">{renderMonthView()}</div>
      )}
      {viewMode === "week" && renderWeekView()}
      {viewMode === "day" && renderDayView()}
    </div>
  );
};

export default Calendar;
