type TypeCardProps = {
  name: string;
  size: string;
  icon?: React.ReactNode;
  accent?: string;
};

function TypeCard({
  name,
  size,
  icon,
  accent = "bg-gray-100 text-gray-600",
}: TypeCardProps) {
  return (
    <div className="flex items-center gap-3 w-full h-16 bg-white rounded-md px-3 shadow-sm">
      <div
        className={`flex items-center justify-center w-10 h-10 rounded-md ${accent}`}
      >
        {icon}
      </div>

      <div className="flex flex-col overflow-hidden">
        <span className="text-sm font-medium truncate">{name}</span>
        <span className="text-xs text-black/50">{size}</span>
      </div>
    </div>
  );
}

export default TypeCard;
