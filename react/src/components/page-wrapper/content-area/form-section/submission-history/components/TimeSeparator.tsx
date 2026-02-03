import TimeFormats from "../../../../../../utils/TimeFormats";

function TimeSeparator({ date }: { date: Date }) {
  return (
    <div className="flex items-center gap-3 my-4">
      <div className="flex-1 h-px bg-black/10" />
      <span className="text-xs text-black/50 whitespace-nowrap">
        {TimeFormats.formatDay(date)} · {TimeFormats.formatTime(date)}
      </span>
      <div className="flex-1 h-px bg-black/10" />
    </div>
  );
}

export default TimeSeparator;
