import TimeSeparator from "./components/TimeSeparator";
import TimeFormats from "../../../../../utils/TimeFormats";
import TypeCard from "./type-cards/TypeCard";
import { FileText, FileType, MessageSquareText } from "lucide-react";

type Submission = {
  id: string;
  name: string;
  size: string;
  type: "pdf" | "txt" | "plain";
  createdAt: Date;
};

type SubmissionHistoryProps = {
  submissions: Submission[];
};

function SubmissionHistory({ submissions }: SubmissionHistoryProps) {
  const getIconAndAccent = (type: "pdf" | "txt" | "plain") => {
    switch (type) {
      case "pdf":
        return {
          icon: <FileText size={20} />,
          accent: "bg-red-100 text-red-600",
        };
      case "txt":
        return {
          icon: <FileType size={20} />,
          accent: "bg-blue-100 text-blue-600",
        };
      case "plain":
        return {
          icon: <MessageSquareText size={20} />,
          accent: "bg-gray-100 text-gray-600",
        };
    }
  };

  return (
    <div className="flex flex-col gap-2">
      {submissions.map((file, index) => {
        const prev = submissions[index - 1];

        const showSeparator =
          !prev ||
          TimeFormats.diffInMinutes(file.createdAt, prev.createdAt) > 60;

        const { icon, accent } = getIconAndAccent(file.type);

        return (
          <div key={file.id}>
            {showSeparator && <TimeSeparator date={file.createdAt} />}

            <TypeCard
              name={file.name}
              size={file.size}
              icon={icon}
              accent={accent}
            />
          </div>
        );
      })}
    </div>
  );
}

export default SubmissionHistory;
