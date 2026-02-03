import TimeSeparator from "./components/TimeSeparator";
import TimeFormats from "../../../../../utils/TimeFormats";
import TypeCard from "./type-cards/TypeCard";

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
  return (
    <div className="flex flex-col gap-2">
      {submissions.map((file, index) => {
        const prev = submissions[index - 1];

        const showSeparator =
          !prev ||
          TimeFormats.diffInMinutes(file.createdAt, prev.createdAt) > 60;

        return (
          <div key={file.id}>
            {showSeparator && <TimeSeparator date={file.createdAt} />}

            <TypeCard
              name={file.name}
              size={file.size}
              icon={file.type === "pdf" ? "pdf" : "txt"}
            />
          </div>
        );
      })}
    </div>
  );
}

export default SubmissionHistory;
