function diffInMinutes(a: Date, b: Date) {
  return Math.abs(a.getTime() - b.getTime()) / 1000 / 60;
}

function formatTime(date: Date) {
  return date.toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
  });
}

function formatDay(date: Date) {
  return date.toLocaleDateString([], {
    weekday: "short",
    month: "short",
    day: "numeric",
  });
}

export default { diffInMinutes, formatTime, formatDay };
